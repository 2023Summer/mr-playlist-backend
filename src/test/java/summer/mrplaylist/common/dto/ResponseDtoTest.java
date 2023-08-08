package summer.mrplaylist.common.dto;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import lombok.AllArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

@Slf4j
class ResponseDtoTest {

	@Test
	public void responseTest() throws Exception {
		//given
		OrderForm orderForm = new OrderForm("뽀또", 3);
		//when
		Response<Object> responseDto = new Response<>(orderForm);
		//then
		log.info("responseDto = {}", responseDto.toString());
	}

	@ToString
	@AllArgsConstructor
	class OrderForm {
		private String name;
		private int count;
	}

	@Test
	public void pageResponseTest() throws Exception {
		//given
		List<OrderForm> orderFormList = new ArrayList<>();
		orderFormList.add(new OrderForm("자가비", 4));
		orderFormList.add(new OrderForm("뽀또", 3));

		PageRequest pageRequest = PageRequest.of(0, 2);
		Page page = new PageImpl(orderFormList, pageRequest, 2);
		//when
		PageResponse pageResponse = new PageResponse<>(page);
		//then

		log.info("PageResponse = {}", (Response)pageResponse);

	}

}
