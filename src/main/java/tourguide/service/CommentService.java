package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.Comment;
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
                .user(
                        new UserDTO().builder()
                                .fullName(comment.getUser().getFullName())
                                .avatar(comment.getUser().getAvatar())
                                .build()
                )
                .build();
        return commentDTO;
    }

    public CommentDTO createComment(CommentDTO commentDTO, Long userId){
        Comment comment = new Comment();
        comment.setIsDelete(false);
        comment.setUser(userService.findById(userId));
        comment.setPost(postService.findById(commentDTO.getPostId()));
        comment.setContent(commentDTO.getContent());

        Comment newComment= commentRepository.save(comment);

        return buildCommentDTO(newComment);
    }

    public List<CommentDTO> getCommentByPosts(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        List<CommentDTO> commentDTOS = new ArrayList<>();
        for(Comment comment: comments){
            commentDTOS.add(buildCommentDTO(comment));
        }
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

    public Boolean deleteComment(Long id, Long userId){
        Comment comment = findById(id);
        if(comment.getUser().getId() != userId){
            throw new BadRequestException("Không thể xóa bình luận này");
        }
        commentRepository.delete(comment);
        return true;
    }
}