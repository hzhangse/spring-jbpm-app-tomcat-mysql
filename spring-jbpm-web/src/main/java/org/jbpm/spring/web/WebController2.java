package org.jbpm.spring.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/test")
public class WebController2 {
	@Autowired
	RuntimeManager runtimeManager;
	
	@RequestMapping(value="/rewards",method = RequestMethod.GET)
	public String printHello(ModelMap model) {
		

		RuntimeEngine engine = runtimeManager.getRuntimeEngine(null);
		KieSession ksession = engine.getKieSession();
		TaskService taskService = engine.getTaskService();

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("recipient", "kylin");
		ksession.startProcess("org.jbpm.demo.rewards", params);

		// let john execute Task 1
		List<TaskSummary> list = taskService.getTasksAssignedAsPotentialOwner("john", "en-UK");
		TaskSummary task = list.get(0);
		taskService.start(task.getId(), "john");
		taskService.complete(task.getId(), "john", null);

		// let mary execute Task 2
		list = taskService.getTasksAssignedAsPotentialOwner("mary", "en-UK");
		task = list.get(0);
		taskService.start(task.getId(), "mary");
		taskService.complete(task.getId(), "mary", null);

		runtimeManager.disposeRuntimeEngine(engine);
		return "index";
	}

}
