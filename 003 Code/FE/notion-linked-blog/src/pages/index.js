import AppLayout from "@/components/AppLayout";
import {Layout} from "antd";
import Head from "next/head";

export default function Home() {
	return (
		<Layout>
			<Head>
				<title>노션 연동 블로그 서비스</title>
			</Head>
			<AppLayout/>
		</Layout>
	);
}
