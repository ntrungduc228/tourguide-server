package tourguide.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tourguide.model.Post;
import tourguide.payload.PostDto;
import tourguide.repository.PostRepository;

import java.util.List;

@Service
public class PostService {

    @Autowired
    PostRepository postRepository;

    public List<Post> getPosts(){
        List<Post> lists= (List<Post>) postRepository.findAll();
        return lists;
    }

    public Post createPost(PostDto postDto){
        Post post = new Post();
        post.setContent(postDto.getContent());
        post.setUsername(postDto.getUsername());
        return postRepository.save(post);
    }

}