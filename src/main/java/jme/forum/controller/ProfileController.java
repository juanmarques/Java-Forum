package jme.forum.controller;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;

import jme.forum.entity.Topic;
import jme.forum.entity.User;
import jme.forum.repository.AnswerRepository;
import jme.forum.repository.TopicRepository;
import jme.forum.repository.UserRepository;

@Controller
public class ProfileController {

    private final UserRepository userRepository;
	private final TopicRepository topicRepository;
	private final AnswerRepository answerRepository;

	@Autowired
	public ProfileController(UserRepository userRepository, TopicRepository topicRepository,
			AnswerRepository answerRepository) {
		this.userRepository = userRepository;
		this.topicRepository = topicRepository;
		this.answerRepository = answerRepository;
	}

	@RequestMapping(value = "profile", method = RequestMethod.GET  )
	public String displayMyProfile(Model model) {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String username = ((UserDetails) principal).getUsername();

		User user = userRepository.getUserByUsername(username);
		Long points = userRepository.getPoints(user.getId());

		Long numberOfTopics = topicRepository.countTopicsByUser_Id(user.getId());
		Long numberOfAnswers = answerRepository.countAnswersByUser_Id(user.getId());
		Long numberOfHelped = answerRepository.countAnswersByUser_IdAndUseful(user.getId(), true);

		model.addAttribute("user", user);
		model.addAttribute("points", points);
		model.addAttribute("numberOfTopics", numberOfTopics);
		model.addAttribute("numberOfAnswers", numberOfAnswers);
		model.addAttribute("numberOfHelped", numberOfHelped);
		return "profile";
	}

	@RequestMapping(value = "profile/{id}", method = RequestMethod.GET  )
	public String displayProfileById(@PathVariable Long id, Model model) {
		User user = userRepository.getUserById(id);
		Long points = userRepository.getPoints(user.getId());

		Long numberOfTopics = topicRepository.countTopicsByUser_Id(id);
		Long numberOfAnswers = answerRepository.countAnswersByUser_Id(id);
		Long numberOfHelped = answerRepository.countAnswersByUser_IdAndUseful(id, true);

		model.addAttribute("user", user);
		model.addAttribute("points", points);
		model.addAttribute("numberOfTopics", numberOfTopics);
		model.addAttribute("numberOfAnswers", numberOfAnswers);
		model.addAttribute("numberOfHelped", numberOfHelped);

		return "profile";
	}

	@PostMapping("profile")
	public View addTask(@RequestParam("category") String category, @RequestParam("title") String title,
			@RequestParam("content") String content, @RequestParam("code") String code,
			@RequestParam("id_user") String id_user, HttpServletRequest request) {
		Topic topic = new Topic();
		topic.setCategory(category);

		// I know that it can be blank field, but I did it on purpose to find
		// out about Optionals:
		if (Objects.equals(code, ""))
			topic.setCode(null);
		else
			topic.setCode(code);

		topic.setContent(content);
		topic.setTitle(title);
		topic.setCreatedDate(LocalDateTime.now());
		topic.setUser(userRepository.getUserById(Long.parseLong(id_user)));

		topicRepository.save(topic);
		String contextPath = request.getContextPath();
		return new RedirectView(contextPath + "/profile");
	}
}