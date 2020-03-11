function loginButtonClicked(){
	username = usernameTextBox.value;
	password = passwordTextBox.value;

	localStorage.setItem("user", username);
	const usernameObj = {
		username: username,
		password: password
	}
console.log(password);
fetch("/api/admin/edit", {
	method: 'POST',
	headers:{
		"Accept": "application/json",
		"Content-Type": "application/json"
	},
	  body: JSON.stringify(usernameObj)
  }).then((response) => {
		console.log(response.status);
	  });
}



let button = document.getElementsByClassName("loginbutton")[0];
let usernameTextBox = button.parentNode.previousSibling.previousSibling.previousSibling.previousSibling.childNodes[3];//.childNodes[1];
let passwordTextBox = button.parentNode.previousSibling.previousSibling.childNodes[3];//.childNodes[1];
console.log(usernameTextBox);
button.addEventListener("click", loginButtonClicked);

