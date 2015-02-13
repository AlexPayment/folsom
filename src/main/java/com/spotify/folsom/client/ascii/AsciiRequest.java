/*
 * Copyright (c) 2014-2015 Spotify AB
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.spotify.folsom.client.ascii;

import com.google.common.base.Charsets;
import com.spotify.folsom.client.Request;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class AsciiRequest<T> extends Request<T> {
  protected static final byte[] NEWLINE_BYTES = "\r\n".getBytes(Charsets.US_ASCII);

  public AsciiRequest(String key) {
    super(key, 0);
  }

  protected static ByteBuf toBufferWithValueAndNewLine(final ByteBufAllocator alloc, ByteBuffer dst,
                                                       byte[] value) {
    ByteBuf buffer = toBuffer(alloc, dst, value.length + NEWLINE_BYTES.length);
    buffer.writeBytes(value);
    buffer.writeBytes(NEWLINE_BYTES);
    return buffer;
  }

  @Override
  public void handle(Object response) throws IOException {
    if (response == null) {
      throw new NullPointerException("response");
    }
    if (!(response instanceof AsciiResponse)) {
      throw new IOException("Unknown response type: " + response.getClass());
    }
    handle((AsciiResponse) response);
  }

  protected abstract void handle(AsciiResponse response) throws IOException;
}