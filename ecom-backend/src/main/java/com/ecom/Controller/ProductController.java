package com.ecom.Controller;

import com.ecom.Model.Product;
import com.ecom.Services.FileUpload;
import com.ecom.Services.ProductService;
import com.ecom.payload.AppConstants;
import com.ecom.payload.ProductDto;
import com.ecom.payload.ProductResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;
	private List<Product> findProductByCategory;

	@Autowired
	private FileUpload fileUpload;

	@Value("${product.path.images}")
	private String imagePath;

	@PostMapping("/images/{productId}")
	 public ResponseEntity<?> uploadImageOfProduct(@PathVariable int productId
		, @RequestParam("product_image")MultipartFile file){
		ProductDto productDto = this.productService.viewProductById(productId);
		String imageName = null;
		try {
			String uploadImage = this.fileUpload.uploadImage(imagePath, file);
			productDto.setImageName(uploadImage);
			System.out.println(productDto.getImageName());
			ProductDto updatedProduct = this.productService.updateProduct(productId, productDto);
			System.out.println(updatedProduct.getImageName());
			return new ResponseEntity<>(updatedProduct,HttpStatus.CREATED);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(Map.of("Message", "File not Upload in server"), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	@PostMapping("/create/{catid}")
	//product/create
	public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto product,@PathVariable int catid) {
		//System.out.println(product.getProductName());
		 ProductDto createProduct = productService.createProduct(product,catid);
		return new ResponseEntity<ProductDto>(createProduct,HttpStatus.CREATED);
	}
	
	//view Product
   @GetMapping("/view")
	public  ProductResponse viewAllProduct(@RequestParam(value="pageNumber",defaultValue =AppConstants.PAGE_NUMBER_STRING,required = false) int pageNumber,
			@RequestParam(value = "pageSize",defaultValue =AppConstants.PAGE_SIZE_STRING,required = false) int pageSize,
			@RequestParam(value = "sortBy" ,defaultValue = AppConstants.SORT_BY_STRING,required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR_STRING,required = false) String sortDir){
	   
		  ProductResponse response=productService.viewAll(pageNumber,pageSize,sortBy,sortDir);
		  
		return response;
	}
   //view ProductById
   @ResponseBody
   @GetMapping("/view/{productId}")
   public ResponseEntity<ProductDto> viewProductById(@PathVariable int productId){
	   
	   ProductDto viewById=productService.viewProductById(productId);
	   				System.out.println(viewById.getCategory().getTitle());
	   return new ResponseEntity<ProductDto>(viewById,HttpStatus.OK);
   }
   
  // delete product
   @DeleteMapping("/del/{productId}")
   public ResponseEntity<String> deleteProduct(@PathVariable int productId){
	  
	   productService.deleteProduct(productId);
	   return new ResponseEntity<String>("Product Deleted",HttpStatus.OK);
   }
   
   // update Product
   @PutMapping("/update/{productId}")
   public ResponseEntity<ProductDto> updateProduct(@PathVariable int productId,@RequestBody ProductDto newproduct ){
	   ProductDto updateProduct = productService.updateProduct(productId,newproduct);
	   return new ResponseEntity<ProductDto>(updateProduct,HttpStatus.ACCEPTED);
   }
   
   //Find product by Categoty wise
   @GetMapping("/category/{catId}")
   public  ProductResponse getProductbyCatgory(@PathVariable int catId,@RequestParam(value="pageNumber",defaultValue =AppConstants.PAGE_NUMBER_STRING,required = false) int pageNumber,
			@RequestParam(value = "pageSize",defaultValue =AppConstants.PAGE_SIZE_STRING,required = false) int pageSize,
			@RequestParam(value = "sortBy" ,defaultValue = AppConstants.SORT_BY_STRING,required = false) String sortBy,
			@RequestParam(value="sortDir",defaultValue = AppConstants.SORT_DIR_STRING,required = false) String sortDir){
	   
	       ProductResponse findProductByCategoty = this.productService.findProductByCategoty(catId,pageSize,pageNumber,sortDir);
	       
	   return findProductByCategoty;
   }
}
