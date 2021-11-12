/*
 * Copyright 2015,2019 agwlvssainokuni
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

public class Stub<T> {

	private Item<T> always;

	private final List<Item<T>> list = new LinkedList<>();

	private boolean repeated = false;

	public boolean hasNext() {
		if (always != null) {
			return true;
		}
		return !list.isEmpty();
	}

	public T next() {
		return doNext().getValue();
	}

	public T peek() {
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

	public String nextScriptEngine() {
		return doNext().getEngine();
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

	private Item<T> doNext() {
		if (always != null) {
			return always;
		}
		if (list.isEmpty()) {
			throw new IllegalStateException("Empty stub");
		}
		Item<T> item = list.remove(0);
		if (isRepeated()) {
			list.add(item);
		}
		return item;
	}

	private Item<T> doPeek() {
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

	public Stub<?> setRepeated(boolean repeated) {
		this.repeated = repeated;
		return this;
	}

	public Stub<T> clear() {
		always = null;
		list.clear();
		return this;
	}

	public <E extends T> Stub<T> alwaysReturn(E value) {
		return alwaysReturn(value, value == null ? null : value.getClass().getCanonicalName());
	}

	public <E extends T> Stub<T> alwaysReturn(E value, String type) {
		Item<T> item = new Item<>();
		item.setValue(value);
		item.setType(type);
		always = item;
		list.clear();
		return this;
	}

	public <E extends T> Stub<T> thenReturn(E value) {
		return thenReturn(value, value == null ? null : value.getClass().getCanonicalName());
	}

	public <E extends T> Stub<T> thenReturn(E value, String type) {
		Item<T> item = new Item<>();
		item.setValue(value);
		item.setType(type);
		list.add(item);
		always = null;
		return this;
	}

	public Stub<T> alwaysScript(String script, String engine) {
		Item<T> item = new Item<>();
		item.setScript(script);
		item.setEngine(engine);
		always = item;
		list.clear();
		return this;
	}

	public Stub<T> thenScript(String script, String engine) {
		Item<T> item = new Item<>();
		item.setScript(script);
		item.setEngine(engine);
		list.add(item);
		always = null;
		return this;
	}

	public Stub<T> alwaysMock(Object mock) {
		Item<T> item = new Item<>();
		item.setMock(mock);
		always = item;
		list.clear();
		return this;
	}

	public Stub<T> thenMock(Object mock) {
		Item<T> item = new Item<>();
		item.setMock(mock);
		list.add(item);
		always = null;
		return this;
	}

	public Stub<T> alwaysThrows(Class<? extends Throwable> klass) {
		Item<T> item = new Item<>();
		item.setThrowable(klass);
		always = item;
		list.clear();
		return this;
	}

	public Stub<T> thenThrows(Class<? extends Throwable> klass) {
		Item<T> item = new Item<>();
		item.setThrowable(klass);
		list.add(item);
		always = null;
		return this;
	}

	static class Item<T> {

		private T value = null;

		private String type = null;

		private String script = null;

		private String engine = null;

		private Object mock = null;

		private Class<? extends Throwable> throwable = null;

		public T getValue() {
			return value;
		}

		public void setValue(T value) {
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
