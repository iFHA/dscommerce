package dev.fernando.dscommerce.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.fernando.dscommerce.dto.CategoryDTO;
import dev.fernando.dscommerce.dto.ProductDTO;
import dev.fernando.dscommerce.tests.TokenUtil;

@SpringBootTest
@AutoConfigureMockMvc
// @Transactional removido por conta de que com ele a verificação de integridade referencial não funciona
public class ProductControllerIT {

    @Autowired
	private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

	@Autowired
	private TokenUtil tokenUtil;

    private long existingId;
	private long nonExistingId;
	private long dependentId;
	
	private String clientUsername;
	private String clientPassword;
	private String adminUsername;
	private String adminPassword;
	
	@BeforeEach
	void setUp() throws Exception {

		existingId = 2L;
		nonExistingId = 100000L;
        dependentId = 3L;
		
		clientUsername = "maria@gmail.com";
		clientPassword = "123456";
		adminUsername = "alex@gmail.com";
		adminPassword = "123456";
	}

	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {

		// String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		ResultActions result =
				mockMvc.perform(get("/products/{id}", existingId)
					// .header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").isNotEmpty());
		result.andExpect(jsonPath("$.description").isNotEmpty());
		result.andExpect(jsonPath("$.price").isNotEmpty());
		result.andExpect(jsonPath("$.imgUrl").isNotEmpty());
		result.andExpect(jsonPath("$.categories").isNotEmpty());
	}

	@Test
	public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

		// String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		ResultActions result =
				mockMvc.perform(get("/products/{id}", nonExistingId)
					// .header("Authorization", "Bearer " + accessToken)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

	@Test
	public void findAllShouldReturnProductMinDTOPageWhenNameEmpty() throws Exception {

		ResultActions result =
				mockMvc.perform(get("/products")
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		
		result.andExpect(jsonPath("$.content[0].id").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].name").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].price").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].imgUrl").isNotEmpty());
	}
	@Test
	public void findAllShouldReturnProductMinDTOPageWhenNameIsNotEmpty() throws Exception {

		ResultActions result =
				mockMvc.perform(get("/products")
					.queryParam("name", "PC Gamer ")
					.queryParam("sort", "name")
					.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
		
		result.andExpect(jsonPath("$.content[0].id").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].name").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].name").value("PC Gamer Alfa"));
		result.andExpect(jsonPath("$.content[0].price").isNotEmpty());
		result.andExpect(jsonPath("$.content[0].imgUrl").isNotEmpty());
	}

	@Test
	public void storeShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {

        ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result =
				mockMvc.perform(post("/products")
                    .content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

    @Test
	public void storeShouldReturnForbiddenWhenClientAuthenticated() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(post("/products")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

    @Test
	public void storeShouldReturnUnprocessableEntityWhenAdminAuthenticatedAndProductIsInvalid() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(post("/products")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}

    @Test
	public void storeShouldReturnCreatedWhenAdminAuthenticatedAndProductIsValid() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(post("/products")
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").isNotEmpty());
		result.andExpect(jsonPath("$.name").isNotEmpty());
		result.andExpect(jsonPath("$.description").isNotEmpty());
		result.andExpect(jsonPath("$.price").isNotEmpty());
		result.andExpect(jsonPath("$.imgUrl").isNotEmpty());
		result.andExpect(jsonPath("$.categories").isNotEmpty());
        result.andExpect(header().string("Location", CoreMatchers.containsString("/products/")));

	}
    
    @Test
	public void updateShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {

        ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);

		ResultActions result =
				mockMvc.perform(put("/products/{id}", existingId)
                    .content(jsonBody)
					.contentType(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnauthorized());
	}

    @Test
	public void updateShouldReturnForbiddenWhenClientAuthenticated() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(put("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}

    @Test
	public void updateShouldReturnUnprocessableEntityWhenAdminAuthenticatedAndProductIsInvalid() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(put("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isUnprocessableEntity());
	}

    @Test
	public void updateShouldReturnNotFoundWhenAdminAuthenticatedAndProductDoesNotExist() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(put("/products/{id}", nonExistingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

    @Test
	public void updateShouldReturnOkWhenAdminAuthenticatedAndProductExistsAndIsValid() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ProductDTO productDTO = new ProductDTO();
		productDTO.setName("Produto teste");
        productDTO.setDescription("Produto teste");
        productDTO.setImgUrl("img teste url");
        productDTO.setPrice(5.0);
        productDTO.addCategory(new CategoryDTO(1L, "teste"));

		String jsonBody = objectMapper.writeValueAsString(productDTO);
		
		ResultActions result =
				mockMvc.perform(put("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.content(jsonBody)
						.contentType(MediaType.APPLICATION_JSON)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").isNotEmpty());
        result.andExpect(jsonPath("$.id").value(existingId));
		result.andExpect(jsonPath("$.name").isNotEmpty());
		result.andExpect(jsonPath("$.name").value(productDTO.getName()));
		result.andExpect(jsonPath("$.description").isNotEmpty());
		result.andExpect(jsonPath("$.description").value(productDTO.getDescription()));
		result.andExpect(jsonPath("$.price").isNotEmpty());
		result.andExpect(jsonPath("$.price").value(productDTO.getPrice()));
		result.andExpect(jsonPath("$.imgUrl").isNotEmpty());
		result.andExpect(jsonPath("$.imgUrl").value(productDTO.getImgUrl()));
		result.andExpect(jsonPath("$.categories").isNotEmpty());
		result.andExpect(jsonPath("$.categories[0].id").value(productDTO.getCategories().get(0).id()));

	}

    @Test
	public void deleteShouldReturnUnauthorizedWhenNoTokenGiven() throws Exception {
		ResultActions result =
				mockMvc.perform(delete("/products/{id}", existingId));

		result.andExpect(status().isUnauthorized());
	}

    @Test
	public void deleteShouldReturnForbiddenWhenClientAuthenticated() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, clientUsername, clientPassword);
		
		ResultActions result =
				mockMvc.perform(delete("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isForbidden());
	}


    @Test
	public void deleteShouldReturnNotFoundWhenAdminAuthenticatedAndProductDoesNotExist() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result =
				mockMvc.perform(delete("/products/{id}", nonExistingId)
						.header("Authorization", "Bearer " + accessToken)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNotFound());
	}

    @Test
	public void deleteShouldReturnNoContentWhenAdminAuthenticatedAndProductExistsAndIsValid() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);
		
		ResultActions result =
				mockMvc.perform(delete("/products/{id}", existingId)
						.header("Authorization", "Bearer " + accessToken)
						.accept(MediaType.APPLICATION_JSON));

		result.andExpect(status().isNoContent());

	}
    @Test
	public void deleteShouldReturnBadRequestWhenAdminAuthenticatedAndProductIsDependent() throws Exception {
	
		String accessToken = tokenUtil.obtainAccessToken(mockMvc, adminUsername, adminPassword);

		mockMvc.perform(delete("/products/{id}", dependentId)
		.contentType(MediaType.APPLICATION_JSON)
		.header("Authorization", "Bearer " + accessToken))
		.andExpect(status().isBadRequest());

	}
}
