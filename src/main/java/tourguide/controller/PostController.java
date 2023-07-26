package tourguide.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Post;
import tourguide.payload.PostDto;
import tourguide.payload.ResponseDTO;
import tourguide.service.PostService;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {

    @Autowired
    PostService postService;

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getPosts() {
        List<Post> lists = postService.getPosts();
        return new ResponseEntity<>(new ResponseDTO(lists), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody PostDto postDto){
        Post post = postService.createPost(postDto);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }
}