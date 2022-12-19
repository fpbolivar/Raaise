import App, { Container } from "next/app";
import React from "react";
import "./../styles/globals.css";
import ClientOnly from "./ClientOnly";

class MyApp extends App {
    render() {
        const { Component, pageProps } = this.props;
        return (
            <>
                <div
                    id="custom-loader"
                    style={{
                        background: `url('/assets/images/loader.gif') no-repeat center center rgba(0, 0, 0, 1)`,
                    }}
                ></div>
                <meta
                    name="viewport"
                    content="width=device-width, initial-scale=1"
                />
                <ClientOnly>
                    <Component {...pageProps} />
                </ClientOnly>
            </>
        );
    }
}
export default MyApp;
