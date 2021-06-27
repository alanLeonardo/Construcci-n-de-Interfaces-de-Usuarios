// eslint-disable-next-line no-unused-vars
import React,{Fragment} from "react";
import {Form,Button} from 'react-bootstrap';
import RegisterStyles from '../utils/RegisterStyles.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import {createUser} from './api/api';
import {Link} from "react-router-dom";
import { withRouter } from "react-router";
import Swal from 'sweetalert2';

const idCardRegExp = "^[0-9]{8}$"
const emailRegExp = "^[\\w.-]{1,20}@\\w{3,7}\\.[a-zA-Z]{2,3}$"
const reseatState = {
	email: "",
	firstName: "",
	lastName: "",
	idCard: "",
	password: "",
	emailError: "",
	firstNameError: "",
	lastNameError: "",
	idCardError:  "",
	passwordError: ""
};

class Register extends React.Component{
state = reseatState;

	constructor(props){
		super(props)
		this.state = {
			error: props.error,
			email: "",
			firstName: "",
			lastName: "",
			idCard: "",
			password: "",
			emailError: "",
			firstNameError: "",
			lastNameError: "",
			idCardError:  "",
			passwordError: ""
		}
		this.handleChange = this.handleChange.bind(this);
		this.handleSubmit = this.handleSubmit.bind(this);
	}



   handleChange(event) {
		this.setState({[event.target.name]: event.target.value});
   }


  validate = () => {
	let firstNameError = "";
	let emailError = "";
	let lastNameError = "";
	let idCardError = "";
	let passwordError = "";

	if(!this.state.idCard.match(idCardRegExp)) {
	   idCardError = "idCard field only accept use numeric characters of 8 digits."
	}

	if (!this.state.email.match(emailRegExp)) {
	  emailError = "invalid email";
	}

	if(!this.state.email) {
	  emailError = "emailError cannot be blank";
	}

	if(!this.state.firstName) {
	  firstNameError = "firstName cannot be blank";
	}

	if(!this.state.lastName) {
	  lastNameError = "lastName cannot be blank";
	}

	if(!this.state.idCard) {
	   idCardError = "idCard cannot be blank";
	}

	if(!this.state.password) {
	  passwordError = "password cannot be blank";
	}

	if (emailError || firstNameError || lastNameError || idCardError || passwordError ) {
	  this.setState({ emailError, firstNameError, lastNameError, idCardError, passwordError });
	  return false;
	}

	return true;
  };

	// Lanza un alerta de que hubo un error en registrar el usario con ese email.
	  swalForError(error) {
		   if (error.message.includes('email registrado')) {
			 Swal.fire(`${error.code} ${error.type}`,
			 `El Email ${this.state.email} ya fue registrado por otro usuario.`, 'error');
		   } else {
			 Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
		   }
	  }

   handleSubmit = event => {
	event.preventDefault();
	 const isValid = this.validate();
	 if (isValid) {

	   createUser({email: this.state.email, firstName: this.state.firstName, lastName: this.state.lastName, idCard: this.state.idCard, password: this.state.password})
	   .then(data => console.log(data))
	   .catch(error => this.swalForError(error));

		this.props.history.push("/login");
			  // clear form
		this.setState(reseatState);

	  }
   };

	render(){  return (
		<div>
		   <Form className="container" onSubmit={this.handleSubmit}  >
			  <Form.Label className="titulo"> DigitalWaller </Form.Label>
			 <Form.Group className="label" controlId="formBasicEmail">
			 <Form.Label> Email </Form.Label>
			   <Form.Control type="email" placeholder="Ingrese un email" value={this.state.email} name="email" onChange={this.handleChange}/>
			   <Form.Label className="text-danger"> {this.state.emailError}  </Form.Label>
			   <Form.Text className="text-muted">
				 Usted debe ingresar un mail.
			   </Form.Text>
			 </Form.Group>
			 <Form.Group className="label" controlId="formBasicFirstName">
			   <Form.Label> FirstName </Form.Label>
			   <Form.Control type="firstName" placeholder="Ingrese un nombre" value={this.state.firstName} name="firstName" onChange={this.handleChange}/>
			   <Form.Label className="text-danger"> {this.state.firstNameError} </Form.Label>
			   <Form.Text className="text-muted">
				 Usted debe ingresar un nombre.
			   </Form.Text>
			 </Form.Group>
			 <Form.Group className="label" controlId="formBasicLastName">
			   <Form.Label> LastName </Form.Label>
			   <Form.Control type="lastName" placeholder="Ingrese un apellido" value={this.state.lastName} name="lastName" onChange={this.handleChange} />
			   <Form.Label className="text-danger"> {this.state.lastNameError} </Form.Label>
			   <Form.Text className="text-muted">
				 Usted debe ingresar un apellido.
			   </Form.Text>
			 </Form.Group>
			 <Form.Group className="label" controlId="formBasicIdCard">
			   <Form.Label> IdCard </Form.Label>
			   <Form.Control type="idCard" placeholder="Ingrese un idCard" value={this.state.idCard} name="idCard" onChange={this.handleChange}/>
			   <Form.Label className="text-danger"> {this.state.idCardError} </Form.Label>
			   <Form.Text className="text-muted">
				 Usted debe ingresar un idCard.
			   </Form.Text>
			 </Form.Group>
			 <Form.Group  className="label" controlId="formBasicPassword">
			   <Form.Label> Password
			   </Form.Label>
			   <Form.Control type="password" placeholder="Ingrese un password" value={this.state.password} name="password" onChange={this.handleChange}/>
			   <Form.Label className="text-danger"> {this.state.passwordError} </Form.Label>
			   <Form.Text className="text-muted">
					Usted debe ingresar un password.
			   </Form.Text>
			 </Form.Group>
			 <Button className="register" variant="btn btn-dark" type="submit" >
			   Registrar
			 </Button>
			 <div>
			   <Link to="/login"> Back</Link>
			 </div>
		   </Form>
		</div>
		)
	}
}

export default withRouter(Register);