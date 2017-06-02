package member;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import member.service.MsgHandle;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestMessage {

	@Autowired  
    private WebApplicationContext context;  
	private MockMvc mockMvc;   
	private MsgHandle msg;



	@Before
	public void setUp() throws Exception {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();   
		msg = (MsgHandle) context.getBean("msgHandle");

	}


	@Test
	public void testMsgProcess() {

		String content = "注册 刘丹 17763376339";
		String result =msg.dealer(content, "222222222222");
		System.out.println(result);
	}

	@Test
	public void testDealer() {
		
		String content = "记录";
		String wechatId = "111111111";
		String result = msg.dealer(content, wechatId);
		System.out.println("查询到记录：\n"+result);
		
		content = "余额";
		result = msg.dealer(content, wechatId);
		System.out.println("查询到余额：\n"+result);
		
		content = "次数";
		result = msg.dealer(content, wechatId);
		System.out.println("查询到次数：\n"+result);
		
	}

}
