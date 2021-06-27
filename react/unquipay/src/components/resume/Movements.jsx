// Modulo para la vista de los movimientos de un usuario.
import React, { Component } from 'react';
import {getTransactions} from '../api/api';
import Swal from 'sweetalert2';

export default class Movements extends Component{
    constructor(props){
        super(props)
        this.state = {
            titulo: 'Transactions',
            transactions: []
            }
        this.user = this.props.user
    }

    componentDidMount(){
        getTransactions(this.user.account.cvu)
        .then(response => {
            this.setState({transactions: response.data})
        }).catch(error => this.swalForError(error))
    }

    // Lanza un alerta de que hubo un error en buscar las transacciones del usuario.
    swalForError(error) {
        Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
    }

    render(){
        return (
            <div>
                <h3>{this.state.titulo}</h3>
                <div>
                    <table className="table table-striped table-dark">
                        <thead className="thead-light">
                            <tr>
                                <th scope="col">Amount</th>
                                <th scope="col">Date</th>
                                <th scope="col">Hour</th>
                                <th scope="col">Description</th>
                                <th scope="col">Full Description</th>
                                <th scope="col">Is Cash Out</th>
                            </tr>
                        </thead>
                        <tbody>
                            { this.state.transactions.map(transaction =>
                                <tr>
                                    <td>{transaction.amount}</td>
                                    <td>{transaction.date}</td>
                                    <td>{transaction.hour}</td>
                                    <td>{transaction.description}</td>
                                    <td>{transaction.fullDescription}</td>
                                    <td>{transaction.cashOut ? 'true' : 'false'}</td>
                                </tr>
                            )}
                        </tbody>
                    </table>
                </div>
            </div>
        )
    }
}
