// Pagina de Resumen de cuenta.
// Es el "home" del usuario loggeado.
import React from 'react'
import {withRouter} from 'react-router-dom'
import ResumeComponent from './ResumeComponent'
import {getUserByCVU} from '../api/api'
import Swal from 'sweetalert2';
class ResumePage extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            user: {},
            loading: true
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
                    !this.state.loading &&  <ResumeComponent user={this.state.user}/>
                }
            </div>
            )
    }
}
export default withRouter(ResumePage)