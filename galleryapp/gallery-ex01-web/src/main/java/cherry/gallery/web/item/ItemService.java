/*
 * Copyright 2019 agwlvssainokuni
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cherry.gallery.web.item;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cherry.fundamental.download.DownloadOperation;

@Service
public class ItemService {

	@Autowired
	private NamedParameterJdbcOperations jdbcOperations;

	@Autowired
	private DownloadOperation downloadOperation;

	@Transactional()
	public long create(Item item) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		jdbcOperations.update("INSERT INTO item_master (name, price) VALUES (:name, :price)",
				new BeanPropertySqlParameterSource(item), keyHolder);
		return keyHolder.getKey().longValue();
	}

	@Transactional()
	public boolean update(long id, Item item) {
		MapSqlParameterSource param = new MapSqlParameterSource("id", id);
		BeanPropertySqlParameterSource src = new BeanPropertySqlParameterSource(item);
		for (String nm : src.getParameterNames()) {
			param.addValue(nm, src.getValue(nm), src.getSqlType(nm), src.getTypeName(nm));
		}
		int count = jdbcOperations.update(
				"UPDATE item_master SET name=:name, price=:price, lock_ver=lock_ver+1 WHERE id=:id AND lock_ver=:lockVer",
				param);
		return count == 1;
	}

	@Transactional()
	public boolean delete(long id) {
		MapSqlParameterSource param = new MapSqlParameterSource("id", id);
		int count = jdbcOperations.update("DELETE FROM item_master WHERE id=:id", param);
		return count == 1;
	}

	@Transactional(readOnly = true)
	public Optional<Item> findById(long id) {
		MapSqlParameterSource param = new MapSqlParameterSource("id", id);
		return jdbcOperations.query("SELECT id, name, price, lock_ver FROM item_master WHERE id=:id", param,
				new BeanPropertyRowMapper<>(Item.class)).stream().findFirst();
	}

	@Transactional(readOnly = true)
	public Items search(ItemListForm form) {
		BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(form);
		List<Item> list = jdbcOperations.query( //
				"SELECT id, name, price, lock_ver FROM item_master" //
						+ " WHERE (:name IS NULL OR :name='' OR name=:name)" //
						+ " AND (:priceFm IS NULL OR price>=:priceFm)" //
						+ " AND (:priceTo IS NULL OR price<=:priceTo)", //
				param, new BeanPropertyRowMapper<>(Item.class));
		return new Items(list);
	}

	@Transactional(readOnly = true)
	public void download(ItemListForm form, HttpServletResponse response) {
		Charset charset = StandardCharsets.UTF_8;
		LocalDateTime timestamp = LocalDateTime.now();
		downloadOperation.download(response, "text/csv", charset, "result{0}.csv", timestamp, out -> {
			BeanPropertySqlParameterSource param = new BeanPropertySqlParameterSource(form);
			return jdbcOperations.query( //
					"SELECT id, name, price, lock_ver FROM item_master" //
							+ " WHERE (:name IS NULL OR :name='' OR name=:name)" //
							+ " AND (:priceFm IS NULL OR price>=:priceFm)" //
							+ " AND (:priceTo IS NULL OR price<=:priceTo)", //
					param, resultSetExtractor(out));
		});
	}

	private ResultSetExtractor<Long> resultSetExtractor(Writer out) {
		return resultSet -> {
			CSVFormat format = CSVFormat.EXCEL.withHeader(resultSet);
			try (CSVPrinter csv = new CSVPrinter(out, format)) {
				int columnCount = resultSet.getMetaData().getColumnCount();
				long count = 0L;
				for (; resultSet.next(); count++) {
					for (int i = 1; i <= columnCount; i++) {
						csv.print(resultSet.getObject(i));
					}
					csv.println();
				}
				return count;
			} catch (IOException ex) {
				throw new UncheckedIOException(ex);
			}
		};
	}

}
