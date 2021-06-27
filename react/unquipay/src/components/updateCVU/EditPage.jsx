import React from 'react'
import {withRouter} from 'react-router-dom'
import {getUserByCVU} from '../api/api'
import Swal from 'sweetalert2'
import EditForm from './EditForm'

class EditPage extends React.Component {
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
                    !this.state.loading &&  <EditForm user={this.state.user} />
                }
            </div>
        )
    }
}
export default withRouter(EditPage)