// Modulo para la vista del ingreso de un usuario de UNQpay.
import React from 'react';
import {loginUser} from '../api/api';
import { Button, Form, FormGroup, Label, Input} from 'reactstrap';
import {withRouter} from 'react-router-dom'
import Swal from 'sweetalert2';
import './Login.css';

class LoginPage extends React.Component {

	constructor(props){
		super(props);
		this.state = {
			email: '',
			password: '',
			emailError: '',
			passwordError: ''
		}
		this.updateEmail = this.updateEmail.bind(this)
		this.updatePassword = this.updatePassword.bind(this)
	}

	updateEmail = (event) => this.setState({email: event.target.value})
	updatePassword = (event) => this.setState({password: event.target.value})
	
	fieldsAreValids = () => {
		let emailError = "";
		let passwordError = "";

		emailError = this.validateEmail();
		passwordError = this.validatePassword();
	
		this.setState({ emailError, passwordError });
		return !(emailError || passwordError);
	}

	emailRegExp = "^[\\w.-]{1,20}@\\w{3,7}\\.[a-zA-Z]{2,3}$"

	validateEmail() {
		if (!this.state.email) {
			return "Email field can't be blank."
		}
		if (!this.state.email.match(this.emailRegExp)) {
			return "Email field must use an e-mail format (firstname_surname@gmail.com)."
		}
	}

	validatePassword() {
		if (!this.state.password) {
			return "Password field can't be blank."
		}
	}

	checkAndLogin = event => {
		event.preventDefault();
		if (this.fieldsAreValids()) {
			this.login()
		}
	}
	
	login() {
		loginUser({ email: this.state.email, password: this.state.password })
		.then(response => {
			this.props.history.push(`/${response.data.account.cvu}/resume`, response.data.account.cvu)
		})
		.catch(error => this.swalForError(error));
	}

	// Lanza un alerta de que hubo un error en el login.
	swalForError(error) {
		const errorTitle = "You can't login"
		try {

			if(error.message.includes('LoginUserAdapter')) {
				Swal.fire(errorTitle, error.message.slice(43), 'error')
			} else {
				Swal.fire(errorTitle, error.message, 'error');
			}
		} catch(error) {
			Swal.fire(errorTitle, "This account doesn't exist", 'error')
		}
	}

	// Redirecciona al componente register.
	toRegister = (event) => {
		event.preventDefault()
		this.props.history.push('/register');
	}

	render() {
		return (
			<div className="Login">
				<Form onSubmit={this.checkAndLogin} className="login-form">
					<p className="parrafo">Sign in</p>
					<FormGroup>
						<Label for="email" className="grey-text">Your email</Label>
						<Input
							type="text"
							id="email"
							value={this.state.email}
							onChange={this.updateEmail}
							placeholder="Escriba su email aqui.."
						/>
						<Label className="text-danger text-sm-right">{this.state.emailError}</Label>
					</FormGroup>
					<FormGroup>
						<Label for="password" className="grey-text">Your password</Label>
						<Input
							type="password"
							id="password"
							value={this.state.password}
							onChange={this.updatePassword}
							placeholder="Escriba su password aqui.."
						/>
						<Label className="text-danger text-sm-right">{this.state.passwordError}</Label>
						<Button className="btn btn-dark" type="submit" block onClick={this.checkAndLogin}>
							Login
						</Button>
						<button className="btn btn-link" type="button" onClick={this.toRegister}>
							Register
						</button>
					</FormGroup>
				
				</Form>
			</div>
		)
	}
}

export default withRouter(LoginPage)