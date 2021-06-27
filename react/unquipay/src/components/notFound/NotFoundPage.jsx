import React from "react"
import './NotFoundPage.css';

export default class NotFoundPage extends React.Component {
    constructor(props){
        super(props)
        this.state = {
            message: "404 Page not found."
        }
    }

    render() {
        return (
            <div className="bodyNotFound">
                <div id="notfound">
		            <div className="notfound">
			            <div className="notfound-404">
				            <h1>404</h1>
				            <h2>Page not found</h2>
			            </div>
			            <a href="/" >Login</a>
		            </div>
	            </div>
            </div>
        )
    }
}