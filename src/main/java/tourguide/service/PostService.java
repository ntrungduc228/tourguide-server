package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tourguide.exception.BadRequestException;
import tourguide.exception.NotFoundException;
import tourguide.model.File;
import tourguide.model.Post;
import tourguide.model.Tour;
import tourguide.model.User;
import tourguide.payload.FileDTO;
import tourguide.payload.PostDTO;
import tourguide.payload.UserDTO;
import tourguide.repository.FileRepository;
import tourguide.repository.PostRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    TourService tourService;

    @Autowired
    UserService userService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    FileRepository fileRepository;

    public List<Post> getPosts(){
        List<Post> lists= (List<Post>) postRepository.findAll();
        return lists;
    }

    public Post findById(Long id){
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty()){
            throw new NotFoundException("Không tìm thấy bài đăng");
        }
        return optionalPost.get();
    }

    public PostDTO createPost(PostDTO postDTO, Long userId){
        Tour tour = tourService.findById(postDTO.getTourId());
        User user = userService.findById(userId);
        if(!tourService.checkUserIsInTour(tour, user)){
            throw  new BadRequestException("Không thể tạo bài đăng trên tour này");
        }
        Post post = new Post();
        post.setContent(postDTO.getContent());
        post.setTour(tour);
        post.setUser(user);
        post.setLikes(0);
        post.setIsDelete(false);

        if(postDTO.getFiles()!= null){
            List<File> fileList = buildFiles(postDTO.getFiles(), post);
            post.setFiles(fileList);
        }else {
            System.out.println("file null");
        }
        Post newPost = postRepository.save(post);

        return buildPostReturn(newPost);
    }

    public List<File> buildFiles(List<FileDTO> fileDTOS, Post post){
        List<File> fileList = new ArrayList<>();
        for(FileDTO fileDTO : fileDTOS){
            fileList.add(buildFile(fileDTO, post));
        }
        return fileList;
    }

    public File buildFile(FileDTO fileDTO, Post post){
        File file = new File();
        System.out.println("link " + fileDTO.getLink());
        file.setLink(fileDTO.getLink());
        file.setPostFile(post);
        return file;
    }

    public PostDTO buildPostReturn(Post post){
        if(post != null){
            List<FileDTO> filesReturn = new ArrayList<>();
            if(post.getFiles()!= null){
                for (File file : post.getFiles()){
                    FileDTO fileDTO = new FileDTO(file.getId(), file.getLink(), file.getCreatedAt(), file.getLastModifiedDate());
                    filesReturn.add(fileDTO);
                }
            }

            Integer commentSize = post.getComments() != null ? post.getComments().size() : 0;

            PostDTO postReturn = new PostDTO(post.getId(), post.getContent(),
                    filesReturn,
                    post.getIsDelete(),
                    post.getTour().getId(), post.getUser().getId(),
                    new UserDTO().builder()
                            .email(post.getUser().getEmail())
                            .role(post.getUser().getRole())
                            .phone(post.getUser().getPhone())
                            .address(post.getUser().getAddress())
                            .avatar(post.getUser().getAvatar())
                            .build(),
                    post.getLikes(),
                    commentSize,
                    post.getCreatedAt(), post.getLastModifiedDate());
            return postReturn;
        }
        throw new BadRequestException("Đã có lỗi");
    }

    public List<PostDTO> getPostsByTourId(Long tourId, Long userId){
        Tour tour = tourService.findById(tourId);
        List<PostDTO> postsReturn = new ArrayList<>();
        List<Post> posts = postRepository.findByTourIdOrderByLastModifiedDateDesc(tour.getId());
        if(posts != null){
            for (Post post : posts){
                if(post.getIsDelete()){continue;}
                postsReturn.add(buildPostReturn(post));
            }
        }
        return postsReturn;
    }

    public PostDTO deletePost(Long postId, Long userId){
        Post post =  findById(postId);
        if(post.getUser().getId() != userId){
            throw new BadRequestException("Không thể xóa post này");
        }
        post.setIsDelete(true);
        Post newPost = postRepository.save(post);
        return buildPostReturn(newPost);
    }

    @Transactional
    public PostDTO updatePost(Long postId, PostDTO postDTO, Long userId){
        Post post = findById(postId);
        if(post.getUser().getId() != userId || post.getIsDelete()){
            throw new BadRequestException("Không thể sua post này");
        }

        if(postDTO.getContent() != null && !postDTO.getContent().isEmpty()){
            post.setContent(postDTO.getContent());
        }

        if(postDTO.getFiles()!= null){
            fileRepository.deleteAllByPostFile(post);
            List<File> fileList = new ArrayList<>();
            fileList = buildFiles(postDTO.getFiles(), post);
            post.setFiles(fileList);
        }

//        if(postDTO.getFiles()!= null){
//            List<File> oldFile = post.getFiles();
//            List<File> fileList = new ArrayList<>();
//            if(oldFile == null || oldFile.size() == 0){
//                // tim file cu
//                for(FileDTO fileDTO : postDTO.getFiles()){
//                    if(fileDTO.getId() != null){
////                        fileList.add();
//                        for(File file:oldFile){
//                            if(file.getId() == fileDTO.getId()){
//                                fileList.add(file); break;
//                            }
//                        }
//                    }
//                }
//                // tim file bi xoa
//
//                // build file moi
//            }
//              fileList = buildFiles(postDTO.getFiles(), post);
//            post.setFiles(fileList);
//        }

        Post newPost = postRepository.save(post);
        return buildPostReturn(newPost);
    }

}