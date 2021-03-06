module.exports = {
  devServer: {
      proxy: {
          "/api": {
              target: "http://localhost:8081",
              secure: false,
              changeOrigin: true,
              headers: {
                  Connection: 'keep-alive'
              }
          }
      }
  }
};