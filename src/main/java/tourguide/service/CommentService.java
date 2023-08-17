package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.Comment;
import tourguide.model.NotificationType;
import tourguide.model.Post;
import tourguide.payload.CommentDTO;
import tourguide.payload.UserDTO;
import tourguide.repository.CommentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    PostService postService;

    @Autowired
    UserService userService;

    @Autowired
    NotificationService notificationService;


    public Comment findById(Long id){
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if(optionalComment.isEmpty()){
            throw new NotFoundException("Không tìm thấy bình luận");
        }
        return  optionalComment.get();
    }

    public CommentDTO buildCommentDTO(Comment comment){
        CommentDTO commentDTO = new CommentDTO().builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId(comment.getPost().getId())
                .parentId(comment.getParentId())
                .user(
                        userService.buildUserDTO(comment.getUser())
                )
                .isDelete(comment.getIsDelete())
                .build();
        return commentDTO;
    }

    public CommentDTO createComment(CommentDTO commentDTO, Long userId){
        Comment comment = new Comment();
        Post post = postService.findById(commentDTO.getPostId());
        comment.setIsDelete(false);
        comment.setUser(userService.findById(userId));
        comment.setPost(post);
        comment.setContent(commentDTO.getContent());
        comment.setParentId(commentDTO.getParentId());

        Comment newComment= commentRepository.save(comment);
        if(comment.getUser().getId() != comment.getPost().getUser().getId()){
            if(commentDTO.getParentId()!= null){
                notificationService.notify(post.getUser().getId(), userId, NotificationType.REPLY_COMMENT);
            }else{
                notificationService.notify(post.getUser().getId(), userId, NotificationType.COMMENT);
            }
        }


        return buildCommentDTO(newComment);
    }

    public List<CommentDTO> getCommentByPosts(Long postId){
        System.out.println("vo dayt roi bha");
        List<Comment> comments = commentRepository.findByPostIdAndIsDeleteOrderByLastModifiedDateAsc(postId, false);

        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment: comments){
            commentDTOS.add(buildCommentDTO(comment));
        }
        System.out.println("comment " +  commentDTOS.size());
        return commentDTOS;
    }

    public CommentDTO updateComment(CommentDTO commentDTO, Long userId){
        Comment comment = findById(commentDTO.getId());
        if(comment.getUser().getId() != userId){
            throw new BadRequestException("Không thể sửa bình luận này");
        }
        comment.setContent(commentDTO.getContent());
        Comment newComment = commentRepository.save(comment);
        return buildCommentDTO(newComment);
    }

    public Comment deleteComment(Long id, Long userId){
        Comment comment = findById(id);
        if(comment.getUser().getId() != userId){
            throw new BadRequestException("Không thể xóa bình luận này");
        }
        comment.setIsDelete(true);
        commentRepository.save(comment);
        return comment;
    }
}