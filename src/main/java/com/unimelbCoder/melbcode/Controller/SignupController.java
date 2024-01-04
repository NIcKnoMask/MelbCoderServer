package com.unimelbCoder.melbcode.Controller;

import com.alibaba.fastjson.JSON;
import com.unimelbCoder.melbcode.bean.User;
import com.unimelbCoder.melbcode.models.dao.UserDao;
import com.unimelbCoder.melbcode.utils.NotifyMsgEvent;
import com.unimelbCoder.melbcode.models.enums.NotifyTypeEnum;
import com.unimelbCoder.melbcode.utils.SpringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.sourceforge.pinyin4j.PinyinHelper;

import java.util.HashMap;

@RestController
public class SignupController {
    @Autowired
    UserDao userDao;

    @RequestMapping("/signup")
    public String signup(@RequestBody User user){
        System.out.println("user ask for sign up");

        System.out.println(user);

        int usCount = userDao.countByUsername(user.getUsername()) + 1;
        String userId = chineseToEnglish(user.getUsername() + "-" + usCount);
        userDao.createUser(userId, user.getUsername(), user.getPassword(), user.getEmail(), user.getRole(),
                user.getAge(), user.getIntroduction());

        HashMap<String, Object> res = new HashMap<>();
        res.put("flag", "ok");

        //可扩展登录后返回其他数据
        System.out.println("successfully insert the row");
        return JSON.toJSONString(res);
    }

    /**
     * 用户注册完毕之后触发事件
     *
     * @param username
     */
    private void processAfterRegister(String username){
        SpringUtils.publishEvent(new NotifyMsgEvent<>(this, NotifyTypeEnum.REGISTER, username));
    }

    private String chineseToEnglish(String originId){
        StringBuilder result = new StringBuilder();

        for (char ch : originId.toCharArray()) {
            // 判断是否是中文字符
            if (Character.toString(ch).matches("[\\u4E00-\\u9FA5]")) {
                // 中文字符转拼音
                String[] pinyinArray = PinyinHelper.toHanyuPinyinStringArray(ch);
                if (pinyinArray != null && pinyinArray.length > 0) {
                    result.append(pinyinArray[0].replaceAll("\\d", ""));
                }
            } else {
                // 英文字符保留不变
                result.append(ch);
            }
        }
        return result.toString();
    }
}
