import React from "react";
import AuthTemplate from "@/components/AuthTemplate";
import AuthForm from "@/components/AuthForm";

const Login = () => {
  return (
    <AuthTemplate>
      <AuthForm type="login" />
    </AuthTemplate>
  );
};

export default Login;
