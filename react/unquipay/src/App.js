import React from 'react';
import { BrowserRouter, Switch, Route, Redirect } from "react-router-dom";
import LoginPage from "./components/login/LoginPage";
import EditPage from "./components/updateCVU/EditPage";
import CashIn from "./components/CashIn";
import TransferPage from "./components/transfer/TransferPage";
import LoyaltyPage from "./components/loyalty/LoyaltyPage"
import Register from "./components/Register"
import ResumePage from './components/resume/ResumePage';
import NotFoundPage from './components/notFound/NotFoundPage';
// import 'bootstrap/dist/css/bootstrap.min.css';
import "bootswatch/dist/cerulean/bootstrap.min.css";
import NavigationBar from './components/NavigationBar'

export default function App() {
  return (
	<div>
	  <BrowserRouter>
		<Switch>
		  	<Route exact path="/"> <Redirect to="/login" /> </Route>
		  	<Route exact path="/login"> <LoginPage /> </Route>
		  	<Route exact path="/register"> <Register /> </Route>
			<Switch>
				<>
				<NavigationBar />
				<Switch>
					<Route exact path="/:cvu/resume"> <ResumePage /> </Route>
					<Route exact path="/:cvu/updatecvu"> <EditPage /> </Route>
					<Route exact path="/:cvu/cashin"> <CashIn /> </Route>
					<Route exact path="/:cvu/transfer"> <TransferPage /> </Route>
					<Route exact path="/:cvu/loyalty"> <LoyaltyPage /> </Route>
					<Route path="*"><NotFoundPage /> </Route>
				</Switch>
				</>
			</Switch>
		</Switch>
	  </BrowserRouter>
	</div>
  );
}