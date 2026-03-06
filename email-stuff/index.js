const express = require("express");
const nodemailer = require("nodemailer");
const cors = require("cors");
const fs = require("fs");
const path = require("path");
const sgMail = require("@sendgrid/mail");

const app = express();
app.use(cors());
app.use(express.json());

sgMail.setApiKey(process.env.SENDGRID_API_KEY);

app.post("/send-email", async ( req, res ) =>{
    const {to, code} = req.body;

    console.log("POST /send-email recebido:", req.body);

    if (!to || !code) {
        return res.status(400).send("Falta o campo 'para quem' ou 'codigo'.");
    }
    try {
        let htmlTemplate = fs.readFileSync(path.join(__dirname, "emailscreen.html"), "utf8");
        htmlTemplate = htmlTemplate.replace("{{CODE}}", code);

        const msg = {
            to: to,
            from: 'tesemestrado.msi2025@gmail.com',
            subject: "O seu código de uso único",
            html: htmlTemplate};

        await sgMail.send(msg);

        console.log("Email enviado com sucesso para:", to);
            res.status(200).send("Email enviado com sucesso!");
          } catch (error) {
            console.error("Erro ao enviar o email:", error);
            res.status(500).send("Erro ao enviar o email.");
          }
        });
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => console.log(`Servidor a ouvir na porta ${PORT}`));