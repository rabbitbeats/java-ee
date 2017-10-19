package com.changWen.springMVC.annotation;

import com.changWen.springMVC.SpringMVC_base.entities.User;
import org.springframework.beans.propertyeditors.PropertiesEditor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.beans.PropertyEditorSupport;
import java.io.IOException;
import java.io.Writer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

/**
 * Spring 为展现层提供的基于 MVC 设计理念的优秀的Web 框架，是目前最主流的 MVC 框架之一
 * •Spring3.0 后全面超越 Struts2，成为最优秀的 MVC 框架
 * •Spring MVC 通过一套 MVC 注解，让 POJO 成为处理请求的控制器，而无须实现任何接口。
 * •支持 REST 风格的 URL 请求
 * •采用了松散耦合可插拔组件结构，比其他 MVC 框架更具扩展性和灵活性
 */
/*
    value是一个数组，里面可以放多个值
    这里的user跟testSessionAttributes里map.put的"user"一样，即value,types,对应（key，value)，只有满足其中一个，就能对应
    如果有SessionAttributes注解，一定要有ModelAttribute注解，不然在测试testModelAttribute时有异常*/
//@SessionAttributes(value = {"user"}, types = {String.class})
@RequestMapping("/annotation")
@Controller//这里的user跟testSessionAttributes里map.put的"user"一样，即value,types,对应（key,value)
public class TestSpringAnnotation {
    private static final String SUCCESS = "SpringMVC_base/success";

    /**Test28
     * redirect:success.jsp: 会完成一个到success.jsp的重定向的操作
     * forward:success.jsp: 会完成一个到success.jsp的转发操作
     */
    @RequestMapping("/testRedirect")
    public String testRedirect(){
        System.out.println("testRedirect");
        return "redirect:/springMVCTest.jsp";
    }
//
//    /**
//     * t执行/views/HelloView
//     *//*
//    @RequestMapping("/testView")
//    public String testView(){
//        System.out.println("testView");
//        return "helloView";//类名第一个小写
//    }

    /**Test24
     * 如果得到jsp页面
     * 1、请求处理方法执行完成后，最终返回一个 ModelAndView对象
     * 2、SpringMVC借助ViewResolver 视图解析器得到最终的视图对象（View）
     * 3、internalResourceViewResolver 视图解析器 prefix + returnVal + 后缀
     */
    @RequestMapping("/testViewAndViewResolver")
    public String testViewAndViewResolver(){
        System.out.println("testViewAndViewResolver");
        return SUCCESS;
    }

    /** ****************************************************************************
     * 处理模型数据
     * •Spring MVC 提供了以下几种途径输出模型数据：
     * –1、ModelAndView: 处理方法返回值类型为 ModelAndView时, 方法体即可通过该对象添加模型数据
     * –2、Map 及 Model: 入参为org.springframework.ui.Model、org.springframework.ui.ModelMap
     *                  或 java.uti.Map 时，处理方法返回时，Map中的数据会自动添加到模型中。
     *
     * –3、@SessionAttributes: 将模型中的某个属性暂存到 HttpSession 中，以便多个请求之间可以共享这个属性
     * –4、@ModelAttribute: 方法入参标注该注解后, 入参的对象就会放到数据模型中
     *
     * 前面两个是将模型数据放在请求域！！里面; SessionAttributes是把模型数据放在Session里面！！！！
     ****************************************************************************/


     /**----------------------------------------------------------------------
     * !!!1. 有 @ModelAttribute 标记的方法, 会在每个目标方法执行之前被 SpringMVC 调用!
     * 2. @ModelAttribute 注解也可以来修饰目标方法 POJO 类型的入参, 其 value 属性值有如下的作用:
     * 1). SpringMVC 会使用 value 属性值在 implicitModel 中查找对应的对象, 若存在则会直接传入到目标方法的入参中.
     * 2). SpringMVC 会一 value 为 key, POJO 类型的对象为 value, 存入到 request 中.
     */
    @ModelAttribute
    public void getUser(@RequestParam(value="id",required=false) Integer id,
                        Map<String, Object> map){
        System.out.println("modelAttribute method");
        if(id != null){
            //模拟从数据库中获取对象
            User user = new User(1, "Tom", "123456", "tom@atguigu.com", 12);
            System.out.println("从数据库中获取一个对象: " + user);

            map.put("user", user);
        }
    }

    /**Test17
     * 运行流程:
     * 1. 执行 @ModelAttribute 注解修饰的方法: 从数据库中取出对象, 把对象放入到了 Map 中. 键为: user
     * 2. SpringMVC 从 Map 中取出 User 对象, 并把表单的请求参数赋给该 User 对象的对应属性.
     * 3. SpringMVC 把上述对象传入目标方法的参数.
     *
     * 注意!!: 在 @ModelAttribute 修饰的方法中, 放入到 Map 时的键需要和目标方法入参类型的第一个字母小写的字符串一致!
     *
     * SpringMVC 确定目标方法 POJO 类型入参的过程
     * 1. 确定一个 key:
     * 1). 若目标方法的 POJO 类型的参数木有使用 @ModelAttribute 作为修饰, 则 key 为 POJO 类名第一个字母的小写
     * 2). 若使用了  @ModelAttribute 来修饰, 则 key 为 @ModelAttribute 注解的 value 属性值.
     * 2. 在 implicitModel 中查找 key 对应的对象, 若存在, 则作为入参传入
     * 1). 若在 @ModelAttribute 标记的方法中在 Map 中保存过, 且 key 和 1 确定的 key 一致, 则会获取到.
     * 3. 若 implicitModel 中不存在 key 对应的对象, 则检查当前的 Handler 是否使用 @SessionAttributes 注解修饰,
     * 若使用了该注解, 且 @SessionAttributes 注解的 value 属性值中包含了 key, 则会从 HttpSession 中来获取 key 所
     * 对应的 value 值, 若存在则直接传入到目标方法的入参中. 若不存在则将抛出异常.
     * 4. ！！！若 Handler 没有标识 @SessionAttributes 注解或 @SessionAttributes 注解的 value 值中不包含 key, 则
     * 会通过反射来创建 POJO 类型的参数, 传入为目标方法的参数
     * 5. SpringMVC 会把 key 和 POJO 类型的对象保存到 implicitModel 中, 进而会保存到 request 中.
     *
     * 源代码分析的流程
     * 1. 调用 @ModelAttribute 注解修饰的方法. 实际上把 @ModelAttribute 方法中 Map 中的数据放在了 implicitModel 中.
     * 2. 解析请求处理器的目标参数, 实际上该目标参数来自于 WebDataBinder 对象的 target 属性
     * 1). 创建 WebDataBinder 对象:
     * ①. 确定 objectName 属性: 若传入的 attrName 属性值为 "", 则 objectName 为类名第一个字母小写.
     * *注意: attrName. 若目标方法的 POJO 属性使用了 @ModelAttribute 来修饰, 则 attrName 值即为 @ModelAttribute
     * 的 value 属性值
     *
     * ②. 确定 target 属性:
     * 	> 在 implicitModel 中查找 attrName 对应的属性值. 若存在, ok
     * 	> *若不存在: 则验证当前 Handler 是否使用了 @SessionAttributes 进行修饰, 若使用了, 则尝试从 Session 中
     * 获取 attrName 所对应的属性值. 若 sessionPojo 中没有对应的属性值, 则抛出了异常.
     * 	> 若 Handler 没有使用 @SessionAttributes 进行修饰, 或 @SessionAttributes 中没有使用 value 值指定的 key
     * 和 attrName 相匹配, 则通过反射创建了 POJO 对象
     *
     * 2). SpringMVC 把表单的请求参数赋给了 WebDataBinder 的 target 对应的属性.
     * 3). *SpringMVC 会把 WebDataBinder 的 attrName 和 target 给到 implicitModel.
     * 近而传到 request 域对象中.
     * 4). 把 WebDataBinder 的 target 作为参数传递给目标方法的入参.
     */
    //这里的测试不能获取到password，要修改后，密码不变，还需要上面的方法
    @RequestMapping("/testModelAttribute")
    public String testModelAttribute(User user1){
        System.out.println("修改: " + user1);
        return SUCCESS;
    }

    /**Test16
     * 若希望在多个请求之间共用某个模型属性数据，则可以在控制器类上(也就是当前类上）标注一个 @SessionAttributes,
     * Spring MVC将在模型中对应的属性暂存到 HttpSession 中。

     * 1@SessionAttributes 除了可以通过属性名指定需要放到会话中的属性外(实际上使用的是 value 属性值),
     * 还可以通过模型属性的对象类型指定哪些模型属性需要放到会话中(实际上使用的是 types 属性值)
     *
     * 注意: 该注解只能放在类的上面. 而不能修饰放方法.
     */
    @RequestMapping("/testSessionAttributes")
    public String testSessionAttributes(Map<String, Object> map){
        User user = new User("Tom", "123456", "tom@atguigu.com", 15);
        map.put("user", user);
        map.put("school", "atguigu");
        return SUCCESS;
    }

    /**Test15
     * 目标方法可以添加 Map 类型(实际上也可以是 Model 类型或 ModelMap 类型)的参数.
     */
    @RequestMapping("/testMap")
    public String testMap(Map<String, Object> map){
        System.out.println(map.getClass().getName());
        map.put("names", Arrays.asList("Tom", "Jerry", "Mike"));

        //在success.jsp里增加names
        return SUCCESS;
    }

    /**Test14
     * 目标方法的返回值可以是 ModelAndView 类型。
     * 其中可以包含视图和模型信息,(可以简单 将数据模型看成Map<Spring,Object>对象)
     * SpringMVC 会把 ModelAndView 的 model 中数据放入到 request 域对象中.
     *
     * 在处理方法的方法体中，可以使用如下方法添加数据模型：
     * ModelAndView addObject（String attributeName,Object attributeValue）；
     * ModelAndView addAllObject（Map<String,?> modelMap）；
     *
     * 可以通过如下设置视图：
     * void setView（View view）；//指定一个具体的视图对象
     * void setViewName（String viewName）；//指定一个逻辑视图名
     */
    @RequestMapping("/testModelAndView")
    public ModelAndView testModelAndView(){
        String viewName = SUCCESS;
        ModelAndView modelAndView = new ModelAndView(viewName);

        //添加（键值对）模型数据到 ModelAndView 中.,在success.jsp里增加
        modelAndView.addObject("time", new Date()); //在目标页面viewName里打印出时间

        return modelAndView;
    }

    /**-------------------------------------------------------------------
     * Test13
     * 可以使用 Serlvet 原生的 API 作为目标方法的参数 具体支持以下类型
     * HttpServletRequest、HttpServletResponse、HttpSession、java.security.Principal
     * Locale、InputStream、OutputStream、Reader、Writer
     * @throws IOException
     */
    @RequestMapping("/testServletAPI")
    public void testServletAPI(HttpServletRequest request,
                               HttpServletResponse response, Writer out) throws IOException {
        System.out.println("testServletAPI, " + request + ", " + response);
        out.write("hello springmvc");
//		return SUCCESS;
    }

    /**Test12
     * Spring MVC 会按请求参数名和 POJO 属性名进行自动匹配， 自动为该对象填充属性值。支持级联属性。
     * 如：dept.deptId、dept.address.tel 等
     */
    @RequestMapping("/testPojo")
    public String testPojo(User user) {
        System.out.println("testPojo: " + user);
        //testPojo: User{id=null, username='112', password='1', email='4', age=4}
        ////成功页面request user: User{id=null, username='112', password='1', email='4', age=4}
        return SUCCESS;
    }

//    @RequestMapping("/testPojo")
//    public String testPojo(@RequestBody User user) {
//        System.out.println("testPojo: " + user);//有异常
//        return SUCCESS;
//    }
//    @RequestMapping("/testPojo")
//    public String testPojo(@RequestBody String user) {
//        System.out.println("testPojo: " + user);
//        //testPojo: username=112&password=1&email=2&age=3&address.city=3&address.province=3
          //成功页面request use没有值
//        return SUCCESS;
//    }

    /**
     * 了解:
     *
     * CookieValue: 映射一个 Cookie 值. 属性同 @RequestParam
     */
    @RequestMapping("/testCookieValue")
    public String testCookieValue(@CookieValue("JSESSIONID") String sessionId) {
        System.out.println("testCookieValue: sessionId: " + sessionId);
        return SUCCESS;
    }

    /**--------------------------------------------------------------------------------------
     * Test10
     * 了解: 映射请求头信息 用法同 @RequestParam
     */
    @RequestMapping("/testRequestHeader")
    public String testRequestHeader(
            @RequestHeader(value = "Accept-Language") String al) {
        System.out.println("testRequestHeader, Accept-Language: " + al);
        return SUCCESS;
    }

    /**Test9
     * Spring MVC 通过分析处理方法的签名，将 HTTP 请求信息绑定到处理方法的相应人参中。
     *
     * 使用 @RequestParam 绑定请求参数值
     * testRequestParam?username=atguigu&age=11
     * 使用 @RequestParam 来映射请求参数.
     *       value 值即请求参数的参数名
     *       required 该参数是否必须. 默认为 true，如果为false，没有age这个参数也能执行
     *       defaultValue 请求参数的默认值，如果没这个参数，要将int改为Integer
     */
    @RequestMapping(value = "/testRequestParam")
    public String testRequestParam(
            @RequestParam(value = "username") String un,
            @RequestParam(value = "age", required = false, defaultValue = "0") int age) {
        System.out.println("testRequestParam, username: " + un + ", age: "
                + age);
        return SUCCESS;
    }

    /**
     * -------------------------------------------------------------------------------------
     * REST：即 Representational State Transfer。（资源）表现层状态转化
     * 资源（Resources）：网络上的一个实体，或者说是网络上的一个具体信息。
     * 可以用一个URI（统一资源定位符）指向它，每种资源对应一个特定的 URI 。要获取这个资源，访问它的URI就可以，
     * 因此 URI 即为每一个资源的独一无二的识别符。
     * 表现层（Representation）：把资源具体呈现出来的形式，叫做它的表现层。如，文本可以用 txt 格式表现，也可以用 HTML 格式、XML 格式、JSON 格式表现
     * 状态转化（State Transfer）：每发出一个请求，就代表了客户端和服务器的一次交互过程。
     * HTTP协议，是一个无状态协议，即所有的状态都保存在服务器端。
     * 因此，如果客户端想要操作服务器，必须通过某种手段，让服务器端发生“状态转化”（State Transfer）。
     * 而这种转化是建立在表现层之上的，所以就是 “表现层状态转化”。
     * 具体说，就是 HTTP 协议里面，四个表示操作方式的动词：GET、POST、PUT、DELETE。
     * 它们分别对应四种基本操作：GET 用来获取资源，POST 用来新建资源，PUT 用来更新资源，DELETE 用来删除资源。
     * <p/>
     * Test8
     * HiddenHttpMethodFilter: 浏览器form表单只支持Get中Post请求，不支持Delete，put请求
     * Spring3.0添加一个过滤器，可以将这些请求转换为标准的http方法，使得支持这四种请求
     * <p/>
     * Rest 风格的 URL. 以 CRUD 为例:
     * 新增: /order           HTTP POST   新增order（用来新建资源）
     * 修改: /order/1         HTTP PUT update?id=1
     * 获取: /order/1         HTTP GET get?id=1   得到id=1的order （用来获取资源）
     * 删除: /order/1(占位符)  HTTP DELETE delete?id=1
     * <p/>
     * 如何发送 PUT 请求和 DELETE 请求呢 ?
     * 1. 需要配置 HiddenHttpMethodFilter
     * 2. 需要发送 POST 请求
     * 3. 需要在发送 POST 请求时携带一个 name="_method" 的隐藏域, 值为 DELETE 或 PUT
     * <p/>
     * 在 SpringMVC 的目标方法中如何得到 id 呢? 使用 @PathVariable 注解，这个注解是restful风格的请求参数，不是真正意义上的请求参数(如上）
     * 有可能不能显示成功的页面，但能输出，是tomcat版本的问题，我用的是tomcat8, 换成tomcat7就可以了
     */
    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.PUT)
    public String testRestPut(@PathVariable Integer id) {
        System.out.println("testRest Put: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.DELETE)
    public String testRestDelete(@PathVariable Integer id) {
        System.out.println("testRest Delete: " + id);
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest", method = RequestMethod.POST)
    public String testRest() {
        System.out.println("testRest POST");
        return SUCCESS;
    }

    @RequestMapping(value = "/testRest/{id}", method = RequestMethod.GET)
    public String testRest(@PathVariable Integer id) {
        System.out.println("testRest GET: " + id);
        return SUCCESS;
    }

    /**
     *------------------------------------------------------------
     * 带占位符的 URL 是 Spring3.0 新增的功能，该功能在pringMVC
     * 向 REST 目标挺进发展过程中具有里程碑的意义
     *
     * PathVariable 可以来映射 URL 中的占位符(例如下面的{id})到目标方法的参数id2中.
     */
    @RequestMapping("/testPathVariable/{id}")
    public String testPathVariable(@PathVariable("id") Integer id2) {
        System.out.println("testPathVariable: " + id2);
        return SUCCESS;
    }

    /**-------------------------------------------------------------------------------
     * "*" 代表在两个路径下可以任意个字符 ant风格的路径，了解*/
//    @RequestMapping("/testAntPath")
//    public String testAntPath() {
//        System.out.println("testAntPath");
//        return SUCCESS;
//    }

    /**
     * 了解: 可以使用 params（表示HTTP协议中的请求参数） 和 headers（表示HTTP协议中的请求头） 来更加精确的映射请求.
     * params 和 headers 支持简单的表达式.
     */
    @RequestMapping(value = "testParamsAndHeaders", params = { "username", "age!=10" },
            headers = { "Accept-Language=en-US,zh;q=0.8" })
    public String testParamsAndHeaders() {
        System.out.println("testParamsAndHeaders");
        return SUCCESS;
    }

    /**
     * RequestMapping 的 value、method、params 及 heads
     * 分别表示HTTP协议中的 请求URL、请求方法、请求参数及请求头的映射条件，
     * 他们之间是与的关系，联合使用多个条件可让请求映射更加精确化。

     * 比较常用: 使用 method 属性来指定请求方式
     */
    @RequestMapping(value = "/testPostMethod", method = RequestMethod.POST)
    public String testMethod(@RequestParam(value = "username") String name) {
        System.out.println("testMethod: " + name);
        return SUCCESS;
    }
    @RequestMapping(value = "/testGetMethod", method = RequestMethod.GET)
    public String testMethod() {
        System.out.println("this is get method ");
        return SUCCESS;
    }

    /**
     * *1. 使用 @RequestMapping 注解来映射请求的 URL
     * 2. 返回值会通过视图解析器解析为实际的物理视图, 对于 InternalResourceViewResolver 视图解析器, 会做如下的解析:
     * 通过 prefix + returnVal + 后缀 这样的方式得到实际的物理视图, 然会做转发操作
     * /WEB-INF/views/success.jsp
     *
     * 1. @RequestMapping 除了修饰方法, 还可来修饰类
     * 1). 类定义处: 提供初步的请求映射信息。相对于 WEB 应用的根目录
     * 2). 方法处: 提供进一步的细分映射信息。 相对于类定义处的 URL。
     * 若类定义处未标注 @RequestMapping，则方法处标记的 URL相对于 WEB 应用的根目录
     *
     * 这个模块运行时可能会有异常，要把jar包放到WEN-INF的lib包里，用Maven好像不行
     */
    @RequestMapping("/testRequestMapping")
    public String testRequestMapping2() {
        System.out.println("testRequestMapping");
        return SUCCESS;
    }

    /**
     * ResponseStatus: 返回一个指定的http response状态码。
     */
    @ResponseStatus(reason="no reason",value=HttpStatus.BAD_REQUEST)
    @RequestMapping("/responsestatus")
    public void responseStatusTest(){

    }

    @RequestMapping("/databind1")
    public ModelAndView databind1(Date date) {

        System.out.println("1111111111111111");
        return null;
    }

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Date.class, new MyDateEditor());
        binder.registerCustomEditor(Double.class, new DoubleEditor());
        binder.registerCustomEditor(Integer.class, new IntegerEditor());
    }

    private class MyDateEditor extends PropertyEditorSupport {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = null;
            try {
                date = format.parse(text);
            } catch (ParseException e) {
                format = new SimpleDateFormat("yyyy-MM-dd");
                try {
                    date = format.parse(text);
                } catch (ParseException e1) {
                }
            }
            setValue(date);
        }
    }

    public class DoubleEditor extends PropertiesEditor {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null || text.equals("")) {
                text = "0";
            }
            setValue(Double.parseDouble(text));
        }

        @Override
        public String getAsText() {
            return getValue().toString();
        }
    }

    public class IntegerEditor extends PropertiesEditor {
        @Override
        public void setAsText(String text) throws IllegalArgumentException {
            if (text == null || text.equals("")) {
                text = "0";
            }
            setValue(Integer.parseInt(text));
        }

        @Override
        public String getAsText() {
            return getValue().toString();
        }
    }
}