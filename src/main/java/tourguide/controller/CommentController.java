package tourguide.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import tourguide.model.Comment;
import tourguide.payload.CommentDTO;
import tourguide.payload.ResponseDTO;
import tourguide.service.CommentService;
import tourguide.service.NotificationService;
import tourguide.utils.JwtUtil;

@RestController
@RequestMapping("api/comments")
public class CommentController {

    @Autowired
    CommentService commentService;

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    NotificationService notificationService;

    @PostMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> createComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        CommentDTO comment = commentService.createComment(commentDTO, userId);
        simpMessagingTemplate.convertAndSend("/topic/post/" + comment.getPostId() + "/comment", comment);

        return new ResponseEntity<>(new ResponseDTO((comment)), HttpStatus.CREATED);
    }

    @GetMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> getCommentByPost(@RequestParam("post") Long postId){
        System.out.println("yeyeeyeyy");
        System.out.println(postId);
        return new ResponseEntity<>(new ResponseDTO(commentService.getCommentByPosts(postId)), HttpStatus.OK);
    }

    @PatchMapping("")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> updateComment(@RequestBody CommentDTO commentDTO, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        CommentDTO comment = commentService.updateComment(commentDTO, userId);
        simpMessagingTemplate.convertAndSend("/topic/post/" + comment.getPostId() + "/comment", comment);
        return new ResponseEntity<>(new ResponseDTO((comment)), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    @PreAuthorize("hasRole('TOURIST') or hasRole('TOURIST_GUIDE')")
    public ResponseEntity<?> deleteComment(@PathVariable Long id, HttpServletRequest request){
        Long userId = jwtUtil.getUserId(jwtUtil.getJwtFromRequest(request));
        Comment comment = commentService.deleteComment(id, userId);
        simpMessagingTemplate.convertAndSend("/topic/post/" + comment.getPost().getId() + "/comment", comment);
        return new ResponseEntity<>(new ResponseDTO((comment)), HttpStatus.OK);
    }

}