package com.triple.mileage;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.triple.mileage.common.response.CommonResponseData;
import com.triple.mileage.common.response.CommonResponseError;
import com.triple.mileage.common.response.CommonResponseList;
import com.triple.mileage.dto.EventDTO;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EventControllerTest {
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@Test
	@DisplayName("(전체) 포인트 부여 히스토리 조회")
	public void getHistoryTest() throws Exception {
		ResultActions resultActions = mockMvc
				.perform(get("/events"))
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseList response = objectMapper.readValue(content, CommonResponseList.class);
		assertNotNull(response, "The class must not be null");
	}
	
	@Test
	@DisplayName("(개인) 포인트 부여 히스토리 조회")
	public void getHistoryByUserTest() throws Exception {
		String userId = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8";
		
		ResultActions resultActions = mockMvc
				.perform(get("/events/{userId}", userId))
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseList response = objectMapper.readValue(content, CommonResponseList.class);
		assertNotNull(response, "The class must not be null");
	}
	
	@Test
	@DisplayName("사용자별 누적 포인트 조회")
	public void getPointByUserTest() throws Exception {
		String userId = "e4d1a64e-a531-46de-88d0-ff0ed70c0bb8";
		
		ResultActions resultActions = mockMvc
				.perform(get("/events/{userId}/points", userId))
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseData response = objectMapper.readValue(content, CommonResponseData.class);
		assertNotNull(response, "The class must not be null");
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 저장 - 타입 체크")
	public void fail_savePointDiffrentTypeTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"CREATE",
				"ADD",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds2(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		log.info(objectMapper.writeValueAsString(eventDto));
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isBadRequest());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseError response = objectMapper.readValue(content, CommonResponseError.class);
		assertTrue(StringUtils.equals(response.getCode(), "NOT_EXIST_TYPE_ERROR"));
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 저장 - 액션 체크")
	public void fail_savePointDiffrentActionTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"REVIEW",
				"UPDATE",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds2(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		log.info(objectMapper.writeValueAsString(eventDto));
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isBadRequest());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseError response = objectMapper.readValue(content, CommonResponseError.class);
		assertTrue(StringUtils.equals(response.getCode(), "NOT_EXIST_ACTION_ERROR"));
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 저장 && 동일 장소 리뷰 저장 체크")
	public void savePointAndAlreadyReviewErrorTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"REVIEW",
				"ADD",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds2(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		ResultActions saveResultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isOk());
		
		String saveContent = saveResultActions.andReturn().getResponse().getContentAsString();
		
		log.info("saveContent : {}", saveContent);
		
		CommonResponseData saveResponse = objectMapper.readValue(saveContent, CommonResponseData.class);
		assertNotNull(saveResponse, "The class must not be null");
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isBadRequest());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseError response = objectMapper.readValue(content, CommonResponseError.class);
		assertTrue(StringUtils.equals(response.getCode(), "ALREADY_REVIEW_ERROR"));
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 수정 - 사진 1장 삭제")
	public void updatePointDeleteOnePhotoTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"REVIEW",
				"MOD",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds1(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseData response = objectMapper.readValue(content, CommonResponseData.class);
		assertNotNull(response, "The class must not be null");
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 수정 - 사진 모두 삭제")
	public void updatePointDeleteAllPhotoTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"REVIEW",
				"MOD",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds0(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseData response = objectMapper.readValue(content, CommonResponseData.class);
		assertNotNull(response, "The class must not be null");
	}
	
	@Test
	@DisplayName("포인트 부여 히스토리 삭제")
	public void deletePointTest() throws Exception {
		EventDTO eventDto = new EventDTO(
				"REVIEW",
				"DELETE",
				"240a0658-dc5f-4878-9381-ebb7b2667772",
				"포인트 적립 테스트!",
				attachedPhotoIds1(),
				"3ede0ef2-92b7-4817-a5f3-0c575361f745",
				"2e4baf1c-5acb-4efb-a1af-eddada31b00f"
		);
		
		ResultActions resultActions = mockMvc
				.perform(
					post("/events")
					.content(objectMapper.writeValueAsString(eventDto))
					.contentType(MediaType.APPLICATION_JSON_VALUE)
				)
				.andExpect(status().isOk());
		
		String content = resultActions.andReturn().getResponse().getContentAsString();
		
		log.info("content : {}", content);
		
		CommonResponseData response = objectMapper.readValue(content, CommonResponseData.class);
		assertNotNull(response, "The class must not be null");
	}
	
	public List<String> attachedPhotoIds2() {
		List<String> list = new ArrayList<>();
		list.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
		list.add("afb0cef2-851d-4a50-bb07-9cc15cbdc332");
		return list;
	}
	
	public List<String> attachedPhotoIds1() {
		List<String> list = new ArrayList<>();
		list.add("e4d1a64e-a531-46de-88d0-ff0ed70c0bb8");
		return list;
	}
	
	public List<String> attachedPhotoIds0() {
		List<String> list = new ArrayList<>();
		return list;
	}

}
