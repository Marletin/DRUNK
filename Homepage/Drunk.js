$(document).ready(function(){

	$("#start_arrow").click(function(){
		$("#slide").animate({"margin-left": "+=-100%"}, 700);


	});

	$("#get_app").click(function(){
		$("#slide").animate({"margin-top": "+=-100%"}, 700);


	});

	$("#info").click(function(){
		$("#slide").animate({"margin-left": "+=-100%"}, 700);


	});

	$("#contact").click(function(){
		$("#slide").animate({"margin-top": "+=-100%" , "margin-left": "+=-100%"}, 700);


	});

	$(".back").click(function(){
		$("#slide").animate({"margin-top": "0%" , "margin-left": "-100%"}, 700);


	});

})
