package cn.acyou.oauth2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author youfang
 * @version [1.0.0, 2020-9-12 下午 04:23]
 **/
@Service
public class UserService implements UserDetailsService {
    private static List<User> userList =  new ArrayList<>();
    @Autowired
    private PasswordEncoder passwordEncoder;
    private static String password;

    @PostConstruct
    public void initData() {
        password = passwordEncoder.encode("123456");
        userList.add(new User("macro", password, AuthorityUtils.commaSeparatedStringToAuthorityList("admin")));
        userList.add(new User("andy", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
        userList.add(new User("mark", password, AuthorityUtils.commaSeparatedStringToAuthorityList("client")));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userList.stream().filter(x -> x.getUsername().equals(username)).findFirst();
        if (user.isPresent()){
            //这里返回一个新用户
            //测试中使用userList中的用户会导致 第一次有了密码后，密码被清除了
            return new User(username, password, user.get().getAuthorities());
        }else {
            throw new UsernameNotFoundException("未找到对应用户");
        }
    }

}
