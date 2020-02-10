## 理念

1. 代理是为了提供额外的或不同的操作而插入的用来代替 "实际" 对象的对象
2. 在动态代理上所做的所有调用都会被重定向到单一的调用处理器上

## pre-requirement

1. 已有一个 Handler 的接口

   ```java
   public interface Handler {
       void handle(String data);
   }
   ```

2. Handler 接口的实现类

   ```java
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
   ```

## evolution

1. **want to analysis Handler performance**

   - inject customization Handler to HandlerProxy and implements Handler interface method
   - and call customization Handler handle method in overwrite methods

   ```java
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
   ```

2. use dynamic proxy

   - implements InvocationHandler

   ```java
   public class HandlerInvocation implements InvocationHandler {
       private static final Logger LOG = LoggerFactory.getLogger(HandlerInvocation.class);
       private final Object target;

       public HandlerInvocation(Object target) {
           this.target = target;
       }

       @Override
       public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
           LOG.info("call method " + method + " ,args " + args);
           long start = System.currentTimeMillis();
           try {
               return method.invoke(this.target, args);
           } finally {
               long end = System.currentTimeMillis();
               System.out.println("cost " + (end - start) + "ms");
           }
       }
   }
   ```

3. test usage

   ```java
   /**
   * This is one of strategy, custom HandlerImpl handler <br>
   * then set it to HandlerProxy and use it. <br>
   *
   * <p>Others, in this case, use HandlerProxy to analysis performance; <br>
   * In HandlerProxy, we aggressive it use HandlerImpl and execute. <br>
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
   ```
