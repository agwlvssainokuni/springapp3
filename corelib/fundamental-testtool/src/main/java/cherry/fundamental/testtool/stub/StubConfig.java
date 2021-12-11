/*
 * Copyright 2015,2021 agwlvssainokuni
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

package cherry.fundamental.testtool.stub;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

public class StubConfig {

	private Item always;

	private final List<Item> list = new LinkedList<>();

	private boolean repeated = false;

	public boolean hasNext() {
		if (always != null) {
			return true;
		}
		return !list.isEmpty();
	}

	public Object next() {
		return doNext().getValue();
	}

	public Object peek() {
		return doPeek().getValue();
	}

	public String peekType() {
		return doPeek().getType();
	}

	public boolean isScript() {
		return StringUtils.isNotBlank(doPeek().getScript());
	}

	public String nextScript() {
		return doNext().getScript();
	}

	public String peekScript() {
		return doPeek().getScript();
	}

	public String peekScriptEngine() {
		return doPeek().getEngine();
	}

	public boolean isMock() {
		return doPeek().getMock() != null;
	}

	public Object nextMock() {
		return doNext().getMock();
	}

	public Object peekMock() {
		return doPeek().getMock();
	}

	public boolean isThrowable() {
		return doPeek().getThrowable() != null;
	}

	public Class<? extends Throwable> nextThrowable() {
		return doNext().getThrowable();
	}

	public Class<? extends Throwable> peekThrowable() {
		return doPeek().getThrowable();
	}

	private Item doNext() {
		if (always != null) {
			return always;
		}
		if (list.isEmpty()) {
			throw new IllegalStateException("Empty stub");
		}
		Item item = list.remove(0);
		if (isRepeated()) {
			list.add(item);
		}
		return item;
	}

	private Item doPeek() {
		if (always != null) {
			return always;
		}
		if (list.isEmpty()) {
			throw new IllegalStateException("Empty stub");
		}
		return list.get(0);
	}

	public boolean isRepeated() {
		return repeated;
	}

	public StubConfig setRepeated(boolean repeated) {
		this.repeated = repeated;
		return this;
	}

	public StubConfig clear() {
		always = null;
		list.clear();
		return this;
	}

	public StubConfig alwaysReturn(Object value) {
		return alwaysReturn(value, value == null ? null : value.getClass().getCanonicalName());
	}

	public StubConfig alwaysReturn(Object value, String type) {
		Item item = new Item();
		item.setValue(value);
		item.setType(type);
		always = item;
		list.clear();
		return this;
	}

	public StubConfig thenReturn(Object value) {
		return thenReturn(value, value == null ? null : value.getClass().getCanonicalName());
	}

	public StubConfig thenReturn(Object value, String type) {
		Item item = new Item();
		item.setValue(value);
		item.setType(type);
		list.add(item);
		always = null;
		return this;
	}

	public StubConfig alwaysScript(String script, String engine) {
		Item item = new Item();
		item.setScript(script);
		item.setEngine(engine);
		always = item;
		list.clear();
		return this;
	}

	public StubConfig thenScript(String script, String engine) {
		Item item = new Item();
		item.setScript(script);
		item.setEngine(engine);
		list.add(item);
		always = null;
		return this;
	}

	public StubConfig alwaysMock(Object mock) {
		Item item = new Item();
		item.setMock(mock);
		always = item;
		list.clear();
		return this;
	}

	public StubConfig thenMock(Object mock) {
		Item item = new Item();
		item.setMock(mock);
		list.add(item);
		always = null;
		return this;
	}

	public StubConfig alwaysThrows(Class<? extends Throwable> klass) {
		Item item = new Item();
		item.setThrowable(klass);
		always = item;
		list.clear();
		return this;
	}

	public StubConfig thenThrows(Class<? extends Throwable> klass) {
		Item item = new Item();
		item.setThrowable(klass);
		list.add(item);
		always = null;
		return this;
	}

	static class Item {

		private Object value = null;

		private String type = null;

		private String script = null;

		private String engine = null;

		private Object mock = null;

		private Class<? extends Throwable> throwable = null;

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public String getScript() {
			return script;
		}

		public void setScript(String script) {
			this.script = script;
		}

		public String getEngine() {
			return engine;
		}

		public void setEngine(String engine) {
			this.engine = engine;
		}

		public Object getMock() {
			return mock;
		}

		public void setMock(Object mock) {
			this.mock = mock;
		}

		public Class<? extends Throwable> getThrowable() {
			return throwable;
		}

		public void setThrowable(Class<? extends Throwable> throwable) {
			this.throwable = throwable;
		}
	}

}
