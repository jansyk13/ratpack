/*
 * Copyright 2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ratpack.server.internal;


import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import sun.misc.Signal;

import java.util.function.Supplier;

@ChannelHandler.Sharable
public class SighupHandler extends ChannelInboundHandlerAdapter {

  private static final String HUP = "HUP";

  private volatile ChannelInboundHandlerAdapter inner;

  public SighupHandler(ChannelInboundHandlerAdapter handlerAdapter, Supplier<ChannelInboundHandlerAdapter> handlerAdapterSupplier) {
    this.inner = handlerAdapter;

    Signal.handle(new Signal(HUP), signal -> this.inner = handlerAdapterSupplier.get());
  }

  @Override
  public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelRegistered(ctx);
  }

  @Override
  public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelUnregistered(ctx);
  }

  @Override
  public void channelActive(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelActive(ctx);
  }

  @Override
  public void channelInactive(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelInactive(ctx);
  }

  @Override
  public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
    this.inner.channelRead(ctx, msg);
  }

  @Override
  public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelReadComplete(ctx);
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
    this.inner.userEventTriggered(ctx, evt);
  }

  @Override
  public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {
    this.inner.channelWritabilityChanged(ctx);
  }

  @Override
  public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
    this.inner.exceptionCaught(ctx, cause);
  }

  @Override
  protected void ensureNotSharable() {
    if (isSharable()) {
      throw new IllegalStateException("ChannelHandler " + getClass().getName() + " is not allowed to be shared");
    }
  }

  @Override
  public boolean isSharable() {
    return this.inner.isSharable();
  }

  @Override
  public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
    this.inner.handlerAdded(ctx);
  }

  @Override
  public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
    this.inner.handlerRemoved(ctx);
  }
}
