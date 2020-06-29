package blogs.microservice.controller;

import blogs.microservice.exception.ResourceNotFoundException;
import blogs.microservice.model.Article;
import blogs.microservice.model.Blog;
import blogs.microservice.repository.BlogRepository;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;

/**
 * Created by manju on 28-06-2020.
 * Maintains creation/deletion/updation of blog
 */

@RestController
public class BlogsArticlesServiceController {
    @Autowired
    private BlogRepository blogRepository;

    @Autowired
    RestTemplate restTemplate;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @GetMapping("/blogs")
    public Page<Blog> getAllBlogs(Pageable pageable) {
        return blogRepository.findAll(pageable);
    }

    @GetMapping("/blogs/{id}")
    public ResponseEntity< Blog > getBlogById(@PathVariable(value = "id") Long blogId)
            throws ResourceNotFoundException {
        Blog blogs = blogRepository.findById(blogId)
                .orElseThrow(() -> new ResourceNotFoundException("No blog information available :: " + blogId));
        return ResponseEntity.ok().body(blogs);
    }

    @PostMapping("/blogs")
    public Blog createBlog(@Valid @RequestBody Blog blog) {
        return blogRepository.save(blog);
    }

    @PutMapping("/blogs/{blogId}")
    public Blog updateBlog(@PathVariable Long blogId, @Valid @RequestBody Blog postRequest) {
        return blogRepository.findById(blogId).map(blog -> {
            blog.setBlogName(postRequest.getBlogName());
            //  blog.setArticlesList(postRequest.getArticlesList());
            return blogRepository.save(blog);
        }).orElseThrow(() -> new ResourceNotFoundException("BlogId " + blogId + " not found"));
    }

    @DeleteMapping("/blogs/{blogId}")
    public ResponseEntity<?> deleteBlog(@PathVariable Long blogId) {
        return blogRepository.findById(blogId).map(blog -> {
            blogRepository.delete(blog);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("PostId " + blogId + " not found"));
    }

    @GetMapping("/blogs/{blogId}/articles")
    public String getAllArticlesByBlogId(@PathVariable(value = "blogId") Long blogId,
                                                Pageable pageable) {
        //return articleRepository.findByBlogId(blogId,pageable);
        String response = "";
        ResponseEntity< Blog > blog = getBlogById(blogId);
        if(null != blog && null !=blog.getBody() && !StringUtils.isEmpty(blog.getBody().getId())) {
             response = restTemplate.exchange("http://articles-service/blogs/{blogId}/articles",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }, blogId).getBody();

            System.out.println("Calling article service internally to fetch all articles info" + response);
        }
        return response;

    }

    @PostMapping("/blogs/{blogId}/articles")
    public String createArticle(@PathVariable (value = "blogId") Long blogId,
                                  @RequestBody Article article) throws JSONException {
        String response = "";
        ResponseEntity< Blog > blog = getBlogById(blogId);
        if(null != blog && null !=blog.getBody() && !StringUtils.isEmpty(blog.getBody().getId())) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            System.out.println("req"+article.toString());
           // JSONObject jsonObject = new JSONObject(article.toString());

            JSONObject jsonRequest = new JSONObject();
            jsonRequest.put("articleName", article.getArticleName());

            HttpEntity<String> request = new HttpEntity<String>(jsonRequest.toString(),headers);

             response = restTemplate.exchange("http://articles-service/blogs/{blogId}/articles",
                    HttpMethod.POST, request, new ParameterizedTypeReference<String>() {
                    }, blogId).getBody();
            System.out.println("Calling article service internally to save article info");

        }
        return response;

    }

    @PutMapping("/blogs/{blogId}/articles/{articleId}")
    public String updateArticle(@PathVariable (value = "blogId") Long blogId,
                                 @PathVariable (value = "articleId") Long articleId,
                                 @Valid @RequestBody Article articleRequest) throws JSONException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        JSONObject jsonRequest = new JSONObject();
        jsonRequest.put("articleName", articleRequest.getArticleName());

        HttpEntity<String> request = new HttpEntity<String>(jsonRequest.toString(),headers);
        String response = "";
        ResponseEntity< Blog > blog = getBlogById(blogId);
        if(null != blog && null !=blog.getBody() && !StringUtils.isEmpty(blog.getBody().getId())) {

            response = restTemplate.exchange("http://articles-service/blogs/{blogId}/articles/{articleId}",
                    HttpMethod.PUT, request, new ParameterizedTypeReference<String>() {
                    }, blogId, articleId).getBody();

            System.out.println("Calling article service internally to update article info" + response);
        }
        return response;
    }


    @GetMapping("/blogs/{blogId}/articles/{articleId}")
    public String fetchArticle(@PathVariable (value = "blogId") Long blogId,
                                           @PathVariable (value = "articleId") Long articleId
    ) {
        String response = "";
        ResponseEntity< Blog > blog = getBlogById(blogId);
        if(null != blog && null !=blog.getBody() && !StringUtils.isEmpty(blog.getBody().getId())) {
             response = restTemplate.exchange("http://articles-service/blogs/{blogId}/articles/{articleId}",
                    HttpMethod.GET, null, new ParameterizedTypeReference<String>() {
                    }, blogId, articleId).getBody();
            System.out.println("Calling article service internally to fetch article info" + response);
        }
        return response;
    }

    @DeleteMapping("/blogs/{blogId}/articles/{articleId}")
    public String deleteArticle(@PathVariable (value = "blogId") Long blogId,
                                           @PathVariable (value = "articleId") Long articleId) {
        String response = "";
        ResponseEntity< Blog > blog = getBlogById(blogId);
        if(null != blog && null !=blog.getBody() && !StringUtils.isEmpty(blog.getBody().getId())) {
            response = restTemplate.exchange("http://articles-service/blogs/{blogId}/articles/{articleId}",
                    HttpMethod.DELETE, null, new ParameterizedTypeReference<String>() {
                    }, blogId, articleId).getBody();
            System.out.println("Calling article service internally to delete article info" + response);
        }
        return response;
    }
}
