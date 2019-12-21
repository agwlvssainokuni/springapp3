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

package cherry.fundamental.mail.queue;

import java.io.IOException;
import java.util.List;

import cherry.fundamental.mail.Attachment;

public interface AttachmentStore {

	void save(long messageId, Attachment... attachments) throws IOException;

	List<AttachedEntry> load(long messageId) throws IOException;

	void delete(long messageId) throws IOException;

}
