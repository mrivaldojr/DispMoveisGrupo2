
// Use Parse.Cloud.define to define as many cloud functions as you want.
// For example:
Parse.Cloud.define("hello", function(request, response) {
	response.success("Hello world!");
});

Parse.Cloud.job("aviso_treino_diario", function(request, status) {

	Parse.Push.send({
		channels: [""],
		data: { alert: "Seu treino Hoje é..." }
	}, {
	success: function() {
		status.success("Alerta enviado com sucesso.");
	},
	error: function(error) {
		console.log(error);
		status.error("Algo deu errado.");
	}
	});
});