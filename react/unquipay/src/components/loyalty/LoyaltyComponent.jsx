// Componente de React para ver los Beneficios.
// Se pueden ver los Beneficios aplicados a la cuenta registrada.
import React from 'react';
import {withRouter} from "react-router-dom"

class LoyaltyComponent extends React.Component {
	constructor(props) {
		super(props)
		this.state = {
			user: this.props.user,
			appliedLoyalties: this.props.user.account.appliedLoyalties
		}
	}

	render() {
		return (
			<div>
				<div>
				  <h3>Applied Loyalties</h3>
					<table className="table table-dark">
						<thead>
							<tr>
								<th scope="col">Name</th>
								<th scope="col">Type</th>
								<th scope="col">Value</th>
								<th scope="col">Min Transactions</th>
								<th scope="col">Min Amount</th>
								<th scope="col">Valid From</th>
								<th scope="col">Valid To</th>
							</tr>
						</thead>
						<tbody>
							{ this.state.appliedLoyalties.map(loyalty =>
								<tr>
									<td>{loyalty.name}</td>
									<td>{loyalty.strategyName}</td>
									<td>{loyalty.strategyValue}</td>
									<td>{loyalty.minNumberOfTransactions}</td>
									<td>{loyalty.minAmountPerTransaction}</td>
									<td>{loyalty.validFrom}</td>
									<td>{loyalty.validTo}</td>
								</tr>
							)}
						</tbody>
					</table>
				</div>
			</div>
		)
	}
}
export default withRouter(LoyaltyComponent);