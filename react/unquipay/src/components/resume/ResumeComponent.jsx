import React from 'react'
import {withRouter} from 'react-router-dom'
import Movements from './Movements'

class ResumeComponent extends React.Component {
    constructor(props) {
        super(props)
        this.state = {
            user: this.props.user
        }
    }

    render() {
        return (
            <div>
                <div>
                    <h1 className="text-center">Resume Page</h1>
                    <h3>Welcome, {this.state.user.firstName} {this.state.user.lastName}.</h3>
                    <h3>Balance: ${this.state.user.account.balance}</h3>
                </div>
                <div>
                    <Movements user={this.state.user}/>
                </div>
            </div>
        )
    }

}

export default withRouter(ResumeComponent)