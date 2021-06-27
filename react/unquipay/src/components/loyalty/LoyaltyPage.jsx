import React from 'react'
import LoyaltyComponent from './LoyaltyComponent'
import { withRouter } from 'react-router-dom'
import {getUserByCVU} from '../api/api'
import Swal from 'sweetalert2';
class LoyaltyPage extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            loading: true,
            user: {}
        }
    }

    componentDidMount() {
        getUserByCVU(this.props.match.params.cvu)
        .then((response) =>  {
            this.setState({user: response.data})
            this.setState({loading: false})
        })
        .catch((error) => this.swalForError(error))
    }

    swalForError(error) {
        Swal.fire(`${error.code} ${error.type}`, error.message, 'error');
    }

    render() {
        return (
        <div>
            {
                !this.state.loading && <LoyaltyComponent user={this.state.user}/>
            }
        </div>
        )
    }
}

export default withRouter(LoyaltyPage);