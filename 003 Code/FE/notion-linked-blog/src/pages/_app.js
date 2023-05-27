import wrapper from "@/redux/store";
import {createGlobalStyle} from "styled-components";
import AppHeader from "@/components/common/AppHeader";
import {Provider} from "react-redux";

const GlobalStyles = createGlobalStyle`
	html,
	body,
	div#__next {
		height: 100%;
	}

	html,
	body {
		margin: 0;
		padding: 0;
	}
`;

function App({Component, ...rest}) {
	const {store, props} = wrapper.useWrappedStore(rest);

	return (
		<Provider store={store}>
			<GlobalStyles />
			<AppHeader />
			<Component {...props.pageProps} />
		</Provider>
	);
}

export default App;
