/*
 * Copyright 2012-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.boot.buildpack.platform.docker.transport;

import java.net.URI;

import org.springframework.util.Assert;

/**
 * Exception throw when the Docker API fails.
 *
 * @author Phillip Webb
 * @author Scott Frederick
 * @since 2.3.0
 */
public class DockerEngineException extends RuntimeException {

	private final int statusCode;

	private final String reasonPhrase;

	private final Errors errors;

	DockerEngineException(String host, URI uri, int statusCode, String reasonPhrase, Errors errors) {
		super(buildMessage(host, uri, statusCode, reasonPhrase, errors));
		this.statusCode = statusCode;
		this.reasonPhrase = reasonPhrase;
		this.errors = errors;
	}

	/**
	 * Return the status code returned by the Docker API.
	 * @return the statusCode the status code
	 */
	public int getStatusCode() {
		return this.statusCode;
	}

	/**
	 * Return the reason phrase returned by the Docker API error.
	 * @return the reasonPhrase
	 */
	public String getReasonPhrase() {
		return this.reasonPhrase;
	}

	/**
	 * Return the Errors from the body of the Docker API error, or {@code null} if the
	 * error JSON could not be read.
	 * @return the errors or {@code null}
	 */
	public Errors getErrors() {
		return this.errors;
	}

	private static String buildMessage(String host, URI uri, int statusCode, String reasonPhrase, Errors errors) {
		Assert.notNull(host, "host must not be null");
		Assert.notNull(uri, "URI must not be null");
		StringBuilder message = new StringBuilder(
				"Docker API call to '" + host + uri + "' failed with status code " + statusCode);
		if (reasonPhrase != null && !reasonPhrase.isEmpty()) {
			message.append(" \"" + reasonPhrase + "\"");
		}
		if (errors != null && !errors.isEmpty()) {
			message.append(" " + errors);
		}
		return message.toString();
	}

}
