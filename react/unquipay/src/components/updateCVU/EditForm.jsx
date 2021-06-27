// Modulo para la vista de modificacion de un usuario.
import React, { Component } from 'react';
import API from '../API';
import Swal from 'sweetalert2'
import { withRouter } from 'react-router-dom'

class EditForm extends Component{
	constructor(props){
		super(props)
		this.state = {
			user: this.props.user,
			firstName: this.props.user.firstName,
			lastName: this.props.user.lastName,
			email: this.props.user.email,
			password: this.props.user.password,
			balance: this.props.user.account.balance,
			cvu: this.props.user.account.cvu,
			inputFirstNameError: '',
			inputLastNameError: '',
			inputEmailError: '',
			inputPasswordError: '',
			editFirstName: '',
			editLastName: '',
			editEmail: '',
			editPassword: ''
		}
		this.updateInputEmail = this.updateInputEmail.bind(this);
		this.updateInputFirstName = this.updateInputFirstName.bind(this);
		this.updateInputLastName = this.updateInputLastName.bind(this);
		this.updateInputPassword = this.updateInputPassword.bind(this);
	}

	swalForError(error) {
		Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
	}

	handleChange = event => {
		this.setState({ cvu: event.target.value });
	}

	// Valida si el campo no esta en blanco.
	validateField = (fieldToValidate) => {
		if(!fieldToValidate) {
		return `Cannot be blank.`
		}
	}

	validateName(fieldToValidate) {
		const nameRegex = /^[a-zA-Z]+$/
		if (!fieldToValidate) {
			return `Cannot be blank.`
		} else if (!nameRegex.test(fieldToValidate)) {
			return `Only allows letters.`
		}
	}

	validateEmail(fieldToValidate) {
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/
		if (!fieldToValidate) {
			return `Cannot be blank.`
		} else if (!emailRegex.test(fieldToValidate.toLowerCase())) {
			return `Only allows e-mail format (ej. name_surname@email.com)`
		}
	}

	// Funcion Auxiliar. Valida que los campos ingresados tengan el formato correcto.
	fieldsAreValids = () => {
		let inputFirstNameError = "";
		let inputLastNameError = "";
		let inputEmailError = "";
		let inputPasswordError = "";

		inputFirstNameError = this.validateName(this.state.firstName);
		inputLastNameError = this.validateName(this.state.lastName);
		inputEmailError = this.validateEmail(this.state.email);
		inputPasswordError = this.validateField(this.state.password);
		
		this.setState(
			{
				inputFirstNameError: inputFirstNameError,
				inputLastNameError: inputLastNameError,
				inputEmailError: inputEmailError,
				inputPasswordError: inputPasswordError
			});
		return !(inputFirstNameError || inputLastNameError || inputEmailError || inputPasswordError);
	};

	checkAndGetAccount = (event) => {
		event.preventDefault()
		if (this.cvuIsValid()) {
		  this.getAccountData(event);
		}
	}

	checkAndPutUserData = (event) => {
		event.preventDefault()
		if (this.fieldsAreValids()) {
		  this.modifyAccountData(event);
		}
	}

	modifyAccountData() {
		const cvu = this.state.cvu
		const headers = {headers: {"Content-Type": "application/json"}}

		API.patch(`account/user/${cvu}`,
		{
			firstName: this.state.firstName,
			lastName: this.state.lastName,
			email: this.state.email,
			password: this.state.password
		}, headers)
			.then(res => Swal.fire('Modificado',`${res.status}`, 'success'))
			.catch(error => Swal.fire(`${error.code} ${error.type}`, error.message, 'error'))
	}

	updateInputFirstName = (event) => this.setState({ firstName: event.target.value })
	updateInputEmail = (event) => this.setState({ email: event.target.value })
	updateInputLastName = (event) => this.setState({ lastName: event.target.value })
	updateInputPassword = (event) => this.setState({ password: event.target.value })

	render(){
		return (
			<div>
				<h1 className="text-dark text-center">Edit User</h1>
				<div>
					<form>
						<div className="form-group w-50 mx-auto">
							<div>
								<label htmlFor="nombre">FirstName:</label>
								<input 
									type="text"
									className="form-control text-center" 
									value={this.state.firstName} 
									id="nombre" 
									placeholder="Nombre" 
									disabled={this.state.editFirstName}
									onChange={this.updateInputFirstName}
									/>
								<span className="text-danger">{this.state.inputFirstNameError}</span>
							</div>
							<div>
								<label htmlFor="apellido">LastName:</label>
								<input 
									type="text"
									className="form-control text-center" 
									value={this.state.lastName}
									id="apellido" 
									placeholder="Apellido" 
									disabled={this.state.editLastName}
									onChange={this.updateInputLastName}
									/>
								<span className="text-danger">{this.state.inputLastNameError}</span>
							</div>
							<div>
								<label htmlFor="email">Email:</label>
								<input 
									type="text"
									className="form-control text-center" 
									value={this.state.email} 
									id="email" 
									placeholder="Email" 
									disabled={this.state.editEmail}
									onChange={this.updateInputEmail}
									/>
								<span className="text-danger">{this.state.inputEmailError}</span>
							</div>
							<div>
								<label htmlFor="password">Password:</label>
								<input
									type="password"
									className="form-control text-center"
									value={this.state.password}
									id="password"
									placeholder="Password"
									disabled={this.state.editPassword}
									onChange={this.updateInputPassword}
									/>
								<span className="text-danger">{this.state.inputPasswordError}</span>
							</div>
							<div>
								<label htmlFor="cvu">CVU:</label>
								<input 
									type="text"
									className="form-control text-center" 
									value={this.state.cvu} 
									id="cvu" 
									disabled={true}
								/>
							</div>
							<div>
								<label htmlFor="balance">Balance:</label>
								<input 
									type="text"
									className="form-control text-center"
									value={this.state.balance} 
									id="balance"
									disabled={true}
								/>
							</div>
							<div>
								<br/>
								<button className="btn btn-dark" onClick={this.checkAndPutUserData}>Modify Account Data</button>
							</div>
						</div>
					</form>
				</div>
			</div>
		)
	}
}

export default withRouter(EditForm);