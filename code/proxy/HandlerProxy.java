package cn.edu.ntu.javase.reflect.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zack <br>
 * @create 2020-02-10 21:59 <br>
 */
public class HandlerProxy implements Handler {
  private static final Logger LOG = LoggerFactory.getLogger(HandlerProxy.class);

  private final Handler handler;

  public HandlerProxy(Handler handler) {
    this.handler = handler;
  }

  @Override
  public void handle(String data) {
    long start = System.currentTimeMillis();
    this.handler.handle(data);
    long end = System.currentTimeMillis();
    LOG.info("cost " + (end - start) + " ms");
  }
}
