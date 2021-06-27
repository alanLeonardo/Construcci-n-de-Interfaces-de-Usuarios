import React from 'react';
import {createTransfer} from '../api/api';
import 'bootstrap/dist/css/bootstrap.min.css';
import Swal from 'sweetalert2';
import {Form, Row, Col, InputGroup, Button} from 'react-bootstrap'
import './Transfer.css'
import { withRouter } from 'react-router-dom'

class TransferForm extends React.Component {
  constructor(props) {
    super(props)
    this.state = {
      error: props.error,
      fromCVU: this.props.user.account.cvu,
      toCVU: '',
      amount: '',
      toCVUError: '',
      amountError: ''
    }
      this.updateToCVU = this.updateToCVU.bind(this)
      this.updateAmount = this.updateAmount.bind(this)
  }
  
  cvuRegExp = "^[0-9]{9}$"

  // Actualiza el campo del CVU al que va a ser transferido un monto de dinero.
  updateToCVU = (event) => this.setState({ toCVU: event.target.value })
    
  // Actualiza el campo del monto de dinero a ser transferido.
  updateAmount = (event) => this.setState({ amount: event.target.value })

  // Valida si el campo del CVU es correcto.
  validateCVU = (fieldToValidate, fieldToCompare) => {
    if(!fieldToValidate) {
      return `CVU field required.`
    }
    if(!fieldToValidate.match(this.cvuRegExp)) {
      return `Use numbers of 9 digits.`
    }
    if(fieldToValidate === fieldToCompare) {
      return `Cannot be the same CVU.`
    }
  }

  // Valida si el monto es correcto.
  validateAmount = (anAmount) => {
    if(!anAmount) {
      return `Amount field required.`
    }
    if (anAmount <= 0) {
      return "Must be higher than zero.";
    }
  }
  
  // Funcion Auxiliar. Valida que los campos ingresados tengan el formato correcto.
  fieldsAreValids = () => {
    let toCVUError = "";
    let amountError = "";

    toCVUError = this.validateCVU(this.state.toCVU, this.state.fromCVU);
    amountError = this.validateAmount(this.state.amount);    
    
    this.setState({ toCVUError, amountError });
    return !( toCVUError || amountError);
  };
  
  // Si los campos son validos, realiza un pedido de transferencia a la API.
  // Lanza un alerta con el resultado.
  checkAndPostTransfer = (event) => {
    event.preventDefault()
    if (this.fieldsAreValids()) {
      this.postTransfer();
    }
  }

  // Realiza un request de POST a la API mediante Axios.
  postTransfer() {
      createTransfer({ fromCVU: this.state.fromCVU, toCVU: this.state.toCVU, amount: this.state.amount })
      .then(data => this.alertForSuccessTransaction(data))
      .catch(error => this.swalForError(error));
  }

  // Lanza un alerta de que la transaccion fue exitosa.
  alertForSuccessTransaction(data) {
    Swal.fire('Transfer completed',
    `From CVU: ${this.state.fromCVU}
    To CVU: ${this.state.toCVU}
    Amount: $${this.state.amount}`, 'success')
  }

  // Retorna los detalles de la transaccion exitosa.
  logSuccessTransfer() {
    return {
      "From CVU": this.state.fromCVU,
      "To CVU": this.state.toCVU,
      "Amount": this.state.amount
    }
  }

  // Lanza un alerta de que hubo un error en la transaccion.
  swalForError(error) {
    if (error.message.includes('have no enough money')) {
      Swal.fire(`${error.code} ${error.type}`,
      `Account with CVU ${this.state.fromCVU} have not enough money to make this transfer.`, 'error');
    } else {
      Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
    }
  }

  toResume = (event) => {
    event.preventDefault()
    this.props.history.push('/resume');
  }

  render() {
    return (
        <div className="Transfer">
          <Form className="transfer-form">

            <p className="title">Transfer</p>
            <p className="subtitle">Register a transfer between two CVUs with an amount.</p>


            <Form.Group as={Row} className="form-group">
              <Form.Label column sm="4" className="grey-text">Your CVU: </Form.Label>
              <Col sm="8">
                <Form.Control plaintext readOnly defaultValue={this.state.fromCVU} />
              </Col>
            </Form.Group>

            <Form.Group as={Row} className="form-group">
              <Form.Label column sm="3">To CVU: </Form.Label>
              <Col sm="8">
                <Form.Control
                  className="form-control"
                  placeholder="an numeric CVU"
                  value={this.state.toCVU}
                  onChange={this.updateToCVU}
                />
              <Form.Label className="text-danger text-center">{this.state.toCVUError}</Form.Label>
              </Col>
            </Form.Group>

            <Form.Group as={Row} className="form-group">
              <Form.Label column sm="3">Amount: </Form.Label>
              <Col sm="8">
                <InputGroup>
                  <InputGroup.Prepend>
                    <InputGroup.Text>$</InputGroup.Text>
                  </InputGroup.Prepend>
                <Form.Control
                  className="form-control"
                  placeholder="an amount"
                  value={this.state.amount}
                  onChange={this.updateAmount}
                />
                </InputGroup>
              <Form.Label className="text-danger text-center">{this.state.amountError}</Form.Label>
              </Col>
              <Button className="btn-primary" onClick={this.checkAndPostTransfer}>Confirm</Button>
            </Form.Group>
          </Form>
      </div>
    )
  }
}
export default withRouter(TransferForm)