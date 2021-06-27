// Componente React que representa a la Barra de Navegacion.
import React from 'react';
import {Navbar, Nav, Button} from 'react-bootstrap';
import {withRouter, NavLink} from 'react-router-dom'

class NavigationBar extends React.Component {
	constructor(props){
		super(props)
		this.state = {
			cvu: '',
			page: ''
		}
		this.getCVU.bind(this)
	}

	componentDidMount() {
		const path = this.props.location.pathname
		this.setState({
			cvu: path.slice(1, 10),
			page: path.slice(11)
		})
	}

	getCVU(cvu) {
		this.setState({cvu: cvu})
	}

	toLogout = (event) => {
		event.preventDefault()
		this.props.history.push('/');
	}

	render() {
		return (
		<div>
			<Navbar bg="dark" expand="lg" variant="dark">
				<Navbar.Brand>UNQpay - Digital Wallet</Navbar.Brand>
				<Navbar.Toggle aria-controls="responsive-navbar-nav" />
				<Navbar.Collapse id="responsive-navbar-nav">
					<Nav className="mr-auto">
          				<ul className="navbar-nav">
            				<li className="nav-item">
								<NavLink
									to={{pathname: `/${this.state.cvu}/resume`}}
									className="nav-link">
									Resume
								</NavLink>
							</li>
            				<li className="nav-item">
								<NavLink
									to={{pathname: `/${this.state.cvu}/loyalty`}}
									className="nav-link">
									Loyalty
								</NavLink>
							</li>
            				<li className="nav-item">
								<NavLink
									to={{pathname: `/${this.state.cvu}/cashin`}}
									className="nav-link">
									Cash In
								</NavLink>
							</li>
							<li className="nav-item">
								<NavLink
									to={{pathname:`/${this.state.cvu}/transfer`}}
									className="nav-link">
									Transfer
								</NavLink>
							</li>
							<li className="nav-item">
								<NavLink
									to={{pathname:`/${this.state.cvu}/updatecvu`}}
									className="nav-link">
									Edit Account
								</NavLink>
							</li>
         				</ul>
					</Nav>
					<Button className="btn-primary" onClick={this.toLogout}>Logout</Button>
				</Navbar.Collapse>
			</Navbar>
		</div>
		)
	}
}
export default withRouter(NavigationBar)