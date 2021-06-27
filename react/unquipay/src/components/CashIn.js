// Modulo para la operacion de Transferencia "Cash In".
import React from 'react';
import {Form,Button} from 'react-bootstrap';
import CashInStyle from '../utils/CashInStyle.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import Swal from 'sweetalert2';
import { withRouter } from 'react-router-dom'
import {createCashIn} from './api/api'

const reseatState = {
    amount: 0.00,
    cardType: "credit",
    cardNumber: "",
    fullName: "",
    securityCode: "",
    endDate: "",
    amountError: "",
    isCardError: "",
    cardNumberError: "",
    fullNameError: "",
    securityCodeError: "",
    endDateError: ""
};

class CashIn extends React.Component{
    state = reseatState;

    constructor(props){
        super(props)
        this.state = {
            amount: 0.00,
            cardType: "credit",
            cardNumber: "",
            fullName: "",
            securityCode: "",
            endDate: "",
            amountError: "",
            cardNumberError: "",
            fullNameError: "",
            securityCodeError: "",
            endDateError: "",
            fromCVU: this.props.match.params.cvu
        };
        this.handleChange = this.handleChange.bind(this);
        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleBack = this.handleBack.bind(this);
        this.handleCard = this.handleCard.bind(this);
    }

    handleChange(event) {
        this.setState({[event.target.name]: event.target.value});
    }

    handleCard(event) {
        this.setState({cardType: event.target.value});
    }

    endDateToLocalDate() {
        const month = this.state.endDate.slice(0,2)
        const year = this.state.endDate.slice(3)
        return new Date(parseInt(year),parseInt(month),1)
    }

    validarEndDate() {

        if(!this.state.endDate) {
            return "EndDate cannot be blank";
        }

        if(!this.state.endDate.match("[0-9]{2}/[0-9]{4}$")) {
            return "EndDate field only use numeric characters with format MM/yyyy.";
        }

        if(this.endDateToLocalDate() < new Date()) {
            return "EndDate Card is deprecated"
        }

    }

    validate = () => {
        let amountError = "";
        let cardNumberError = "";
        let fullNameError = "";
        let securityCodeError = "";
        let endDateError = "";

        endDateError = this.validarEndDate()


        if(!this.state.cardNumber) {
            cardNumberError = "CardNumberError cannot be blank";
        }

        if(!this.state.fullName) {
            fullNameError = "FullNameError cannot be blank";
        }

        if(!this.state.securityCode) {
            securityCodeError = "SecurityCodeError cannot be blank";
        }

        if(!this.state.cardNumber.match("^[0-9]{4} [0-9]{4} [0-9]{4} [0-9]{4}$")) {
            cardNumberError = "CardNumber field only accepts a card number format (example:1234 1234 1234 1234).";
        }

        if(!this.state.fullName.match("^([a-zA-Z0-9]+|[a-zA-Z0-9]+\\s{1}[a-zA-Z0-9]{1,}|[a-zA-Z0-9]+\\s{1}[a-zA-Z0-9]{3,}\\s{1}[a-zA-Z0-9]{1,})$")) {
            fullNameError = "FullName field only accept letters and spaces.";
        }


        if(!this.state.securityCode.match("^[0-9]{3}$")) {
            securityCodeError = "SecurityCode field only use numeric characters of 3 digits.";
        }

        if(!this.state.amount > 0.00) {
            amountError = "Amount can't be zero or negative.";
        }

        this.setState({ amountError, cardNumberError, fullNameError, securityCodeError, endDateError });
        return !(amountError || cardNumberError || fullNameError || securityCodeError || endDateError)
    };

    handleBack= event => {
        event.preventDefault();
        this.props.history.push(`/${this.state.fromCVU}/resume`)
    }

    // Lanza un alerta de que hubo un error en registrar el usario con ese email.
    swalForError(error) {
        Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
    }

    handleSubmit = event => {
        event.preventDefault();
        const isValid = this.validate();
        if (isValid) {
            createCashIn({amount: this.state.amount, isCreditCard: this.state.cardType, cardNumber: this.state.cardNumber, fullName: this.state.fullName, securityCode: this.state.securityCode, endDate: this.state.endDate, fromCVU: this.state.fromCVU})
                .then(data => this.props.history.push(`/${this.state.fromCVU}/resume`))
                .catch(error => this.swalForError(error));

            // clear form
            this.setState(reseatState);
        }
    };

    render(){
        return (
            <div>
                <Form className="containerCashin" onSubmit={this.handleSubmit}>
                    <div className="titulo">  Cash in </div>
                    <Form.Group className="label" controlId="formBasicAmount">
                        <Form.Label>Amount</Form.Label>
                        <Form.Control type="amount" placeholder="Ingrese un monto" value={this.state.amount} name="amount" onChange={this.handleChange} />
                        <Form.Label className="text-danger"> {this.state.amountError}  </Form.Label>
                        <Form.Text className="text-muted">
                            Usted debe ingresar un monto.
                        </Form.Text>

                        <select value={this.state.cardType}
                                onChange={this.handleCard}>
                            <option value="credit" > Credit Card </option>
                            <option value="debit" > Debit Card </option>
                        </select>
                    </Form.Group>

                <Form.Label className="text-danger"> {this.state.isCardError}  </Form.Label>
                <Form.Group className="label" controlId="formBasicCardNumber">
                    <Form.Label>Card Number</Form.Label>
                    <Form.Control type="cardNumber" placeholder="Ingrese un numero de documento" value={this.state.cardNumber} name="cardNumber" onChange={this.handleChange} />
                    <Form.Label className="text-danger"> {this.state.cardNumberError} </Form.Label>
                    <Form.Text className="text-muted">
                        Usted debe ingresar un numero de documento.
                    </Form.Text>
                </Form.Group>

                <Form.Group className="label" controlId="formBasicFullName">
                    <Form.Label>Full Name</Form.Label>
                    <Form.Control type="fullName" placeholder="Ingrese su nombre completo" value={this.state.fullName} name="fullName" onChange={this.handleChange}/>
                    <Form.Label className="text-danger"> {this.state.fullNameError}  </Form.Label>
                    <Form.Text className="text-muted">
                        Usted debe ingresar su nombre completo.
                    </Form.Text>
                </Form.Group>
                <div className="dosColumnas">
                    <Form.Group className="columnLabel" controlId="formBasicSecurityCode">
                        <Form.Label>Security Code</Form.Label>
                        <Form.Control type="securityCode" placeholder="Ingrese un codigo de seguridad" value={this.state.securityCode} name="securityCode" onChange={this.handleChange}/>
                        <Form.Label className="text-danger"> {this.state.securityCodeError} </Form.Label>
                        <Form.Text className="text-muted">
                            Usted debe ingresar un codigo de seguridad.
                        </Form.Text>
                    </Form.Group>
                    <Form.Group className="columnLabel" controlId="formBasicEndDate">
                        <Form.Label>End Date</Form.Label>
                        <Form.Control type="endDate" placeholder="Ingrese la fecha de vencimiento" value={this.state.endDate} name="endDate" onChange={this.handleChange}/>
                        <Form.Label className="text-danger"> {this.state.endDateError} </Form.Label>
                        <Form.Text className="text-muted">
                            Usted debe Ingresar la fecha de vencimiento.
                        </Form.Text>
                    </Form.Group>
                </div>
                <div className="dosColumnasButton">
                    <Button variant="btn btn-secondary" type="button" className="buttons" onClick={this.handleBack}>
                        Cancel
                    </Button>
                    <Button variant="btn btn-dark" type="submit" className="buttons">
                        Comfirm
                    </Button>
                </div>
                </Form>
            </div>
        )
    }
}
export default withRouter(CashIn);