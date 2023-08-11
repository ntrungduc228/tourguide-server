package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Post;
import tourguide.payload.PostDTO;
import tourguide.payload.ResponseDTO;
import tourguide.service.PostService;
import tourguide.utils.JwtUtil;

import java.util.List;

@RestController
@RequestMapping("api/posts")
public class PostController {
    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    PostService postService;

//    @GetMapping("")
//    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
//    public ResponseEntity<?> getPosts() {
//        List<Post> lists = postService.getPosts();
//        return new ResponseEntity<>(new ResponseDTO(lists), HttpStatus.OK);
//    }

    @PostMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createPost(@RequestBody PostDTO postDto, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        PostDTO post = postService.createPost(postDto, userId);
        return new ResponseEntity<>(post, HttpStatus.CREATED);
    }

    @PatchMapping("{id}")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updatePost(@PathVariable Long id ,@RequestBody PostDTO postDto, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        PostDTO post = postService.updatePost(id, postDto, userId);
        return new ResponseEntity<>(post, HttpStatus.OK);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getPostsByTourId(@RequestParam("tour") Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        return new ResponseEntity<>(new ResponseDTO(postService.getPostsByTourId(id, userId)), HttpStatus.OK);
    }
}