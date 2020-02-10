package cn.edu.ntu.javase.reflect.proxy;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Proxy;

/**
 * This is one of strategy, custom HandlerImpl1 handler <br>
 * then set it to HandlerProxy and use it. <br>
 *
 * <p>Others, in this case, use HandlerProxy to analysis performance; <br>
 * In HandlerProxy, we aggressive it use HandlerImpl1 and execute. <br>
 *
 * <p>And can use Dynamic proxy to optimize this code, <br>
 * because it can dynamically create proxies and dynamically handle calls to proxied methods. <br>
 *
 * @author zack <br>
 * @create 2020-02-10 22:02 <br>
 */
public class ProxyStrategyTest {
  private static final Logger LOG = LoggerFactory.getLogger(ProxyStrategyTest.class);

  @Test
  public void testHandlerProxy() {
    Handler handler = new HandlerImpl();
    Handler proxy = new HandlerProxy(handler);
    proxy.handle("Test");
  }

  @Test
  public void testHandlerInvocation() {
    Handler handler = new HandlerImpl();
    HandlerInvocation invocationHandler = new HandlerInvocation(handler);

    Handler proxy =
        (Handler)
            Proxy.newProxyInstance(
                // Get ClassLoader: common target object and always interface
                // [The class loader of the proxied object]
                ProxyStrategyTest.class.getClassLoader(),
                // Get an array of Classes of interfaces implemented by the proxied object:
                // target.getClass().getInterfaces()
                new Class<?>[] {Handler.class},
                // Create an InvocationHandler object(usually using an anonymous inner class)
                invocationHandler);

    LOG.info("invoke method");
    proxy.handle("Test");

    Class cls = Proxy.getProxyClass(ProxyStrategyTest.class.getClassLoader(), Handler.class);
    LOG.info("isProxyClass: " + Proxy.isProxyClass(cls));
    LOG.info("getInvocationHandler: " + (invocationHandler == Proxy.getInvocationHandler(proxy)));
  }
}
