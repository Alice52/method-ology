package cn.edu.ntu.javase.reflect.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * @author zack <br>
 * @create 2020-02-10 22:01 <br>
 */
public class HandlerImpl implements Handler {
  private static final Logger LOG = LoggerFactory.getLogger(HandlerImpl.class);

  @Override
  public void handle(String data) {
    try {
      TimeUnit.MILLISECONDS.sleep(100);
      LOG.info(data);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
