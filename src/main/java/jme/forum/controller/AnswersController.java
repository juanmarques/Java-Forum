package jme.forum.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jme.forum.entity.Answer;
import jme.forum.repository.AnswerRepository;

@Controller
public class AnswersController {


	@Autowired
	public final AnswerRepository answerRepository;
	
	@Autowired
	public AnswersController(AnswerRepository answerRepository) {
		this.answerRepository = answerRepository;
	}

	@RequestMapping(value = "answers/{id}", method = RequestMethod.GET)
	public String displayAnswersByUser(@PathVariable String id, Model model) {
		List<Answer> answers = answerRepository.findAnswerByUser_IdOrderByCreatedDateDesc(Long.parseLong(id));
		model.addAttribute("answers", answers);
		return "answers";
	}

	@RequestMapping(value = "answers/useful/{id}", method = RequestMethod.GET)
	public String displayUsefulAnswersByUser(@PathVariable String id, Model model) {
		List<Answer> answers = answerRepository.findAnswerByUser_IdAndUsefulOrderByCreatedDateDesc(Long.parseLong(id),
				true);
		model.addAttribute("answers", answers);
		return "answers";
	}
}