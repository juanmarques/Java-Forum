package jme.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jme.forum.entity.Topic;
import jme.forum.repository.AnswerRepository;
import jme.forum.repository.TopicRepository;

@Controller
public class TopicsController {

	private final TopicRepository topicRepository;
	private final AnswerRepository answerRepository;

	@Autowired
	public TopicsController(TopicRepository topicRepository, AnswerRepository answerRepository) {
		this.topicRepository = topicRepository;
		this.answerRepository = answerRepository;
	}

	@RequestMapping(value = "topics", method = RequestMethod.GET  )
	public String displayAllTopics(Model model) {
		List<Topic> topics = topicRepository.findAll(new Sort(Sort.Direction.DESC, "createdDate"));
		String header = setHeader("all");
		model.addAttribute("topics", topics);
		model.addAttribute("header", header);
		model.addAttribute("answerRepository", answerRepository);
		return "topics";
	}

	@RequestMapping(value = "topics/{category}", method = RequestMethod.GET  )
	public String displayTopicsByCategory(@PathVariable String category, Model model) {
		List<Topic> topics = topicRepository.findTopicsByCategoryOrderByCreatedDateDesc(category);
		String header = setHeader(category);
		model.addAttribute("topics", topics);
		model.addAttribute("header", header);
		model.addAttribute("answerRepository", answerRepository);
		return "topics";
	}

	@RequestMapping(value = "topics/user/{id}", method = RequestMethod.GET  )
	public String displayTopicsByUser(@PathVariable String id, Model model) {
		List<Topic> topics = topicRepository.findTopicsByUser_IdOrderByCreatedDateDesc(Long.valueOf(id));
		String header = setHeader("user");
		model.addAttribute("topics", topics);
		model.addAttribute("header", header);
		model.addAttribute("answerRepository", answerRepository);
		return "topics";
	}

	private String setHeader(String category) {
		switch (category) {
		case "se":
			return "Java Standard Edition";
		case "ee":
			return "Java Enterprise Edition";
		case "jpa":
			return "Java Persistence API and Hibernate";
		case "spring":
			return "Spring Framework";
		case "web":
			return "HTML/CSS/JavaScript";
		case "all":
			return "All topics";
		default:
			return "User's topics";
		}
	}
}