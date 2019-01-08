package top.by.xrh;

import org.junit.Test;
import top.by.xrh.common.RequestMethod;
import top.by.xrh.model.ApiResponseData;
import top.by.xrh.util.RestfulHttpClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Title: AppTest</p>
 * <p>Description: 测试四种请求方式</p>
 *
 * @author zwp
 * @date 2019/1/8 10:56
 */
public class AppTest {

    public final String base_url = "http://127.0.0.1:8081/sbr";

    @Test
    public void testPost() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("account", "restful");
        map.put("name", "测试restful插入数据");
        map.put("pwd", "123456");
        ApiResponseData responseData = RestfulHttpClient.send(base_url + "/restfulUser", RequestMethod.POST, map, Object.class);
        System.out.println(responseData.getData());
    }

    @Test
    public void testDelete() throws Exception {
        ApiResponseData responseData = RestfulHttpClient.send(base_url + "/restfulUser/2", RequestMethod.DELETE, null, Object.class);
        System.out.println(responseData);
        System.out.println(responseData.getData());
        System.out.println(responseData.getCode());
    }

    @Test
    public void testPut() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "9");
        map.put("account", "restful");
        map.put("name", "测试restful修改数据");
        map.put("pwd", "123456");
        ApiResponseData responseData = RestfulHttpClient.send(base_url + "/restfulUser", RequestMethod.PUT, map, Object.class);
        System.out.println(responseData);
    }

    @Test
    public void testGet() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        map.put("id", "9");
        ApiResponseData responseData = RestfulHttpClient.send(base_url + "/restfulUser", RequestMethod.GET, map, Object.class);
        System.out.println(responseData);
        System.out.println(responseData.getData());
        System.out.println(responseData.getCode());
    }

    @Test
    public void testGetAll() throws Exception {
        Map<String, String> map = new HashMap<String, String>();
        ApiResponseData<List> responseData = RestfulHttpClient.send(base_url + "/restfulUser/findAll", RequestMethod.GET, map, List.class);
        System.out.println(responseData);
        System.out.println(responseData.getData());
        System.out.println(responseData.getCode());
        System.out.println("===");
        List list = responseData.getData();
        if (list != null) {
            for (Object o: list
                    ) {
                System.out.println(o);
            }
        }
    }
}

// package top.by.xiceos.controller;
//
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.*;
// import top.by.xiceos.dao.UserDao;
// import top.by.xiceos.entity.User;
// import java.util.List;
//
// /**
//  * <p>Title: RestfulUserController</p>
//  * <p>Description: Restful 风格</p>
//  *
//  * @author zwp
//  * @date 2019/1/8 10:26
//  */
// @Controller
// @RequestMapping(value = "/restfulUser")
// public class RestfulUserController {
//
//     @Autowired
//     private UserDao userDao;
//
//     @ResponseBody
//     @PostMapping
//     public User insert(@ModelAttribute User user) {
//         return userDao.save(user);
//     }
//
//     @ResponseBody
//     @DeleteMapping(value = "/{id}")
//     public void delete(@PathVariable Long id) {
//         User user = new User();
//         user.setId(id);
//         userDao.delete(user);
//     }
//
//     @ResponseBody
//     @PutMapping
//     public void update(@ModelAttribute User user) {
//         userDao.save(user);
//     }
//
//     @ResponseBody
//     @GetMapping
//     public User findById(@ModelAttribute User user) {
//         return userDao.findById(user.getId()).get();
//     }
//
//     @ResponseBody
//     @GetMapping(value = "/findAll")
//     public List<User> findAll() {
//         return userDao.findAll();
//     }
//
//     @ResponseBody
//     @GetMapping(value = "/findByName")
//     public User findByName(@RequestParam(value = "name") String name) {
//         return userDao.findByName(name);
//     }
//
//     @ResponseBody
//     @GetMapping(value = "/findTwoName")
//     public List<User> findTwoName(@RequestParam(value = "name1") String name1, @RequestParam(value = "name2") String name2) {
//         return userDao.findTwoName(name1, name2);
//     }
// }
