import logo from './logo.svg';
import './App.css';
import { useState } from "react";

import Box from "@mui/material/Box";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import ButtonGroup from "@mui/material/ButtonGroup";
import Stack from "@mui/material/Stack";

function App() {
  const [word, setWord] = useState("Click know to start");

  const next = () => {
    fetch("http://localhost:8080/top1000/next", {
        method: 'POST',
        credentials: 'include'
    })
      .then((response) => response.text())
      .then((response) => {
        setWord(response);
      })
      .catch(() => {
        setWord("Something wrong with internet connection");
      });
  };

  const prev = () => {
    fetch("http://localhost:8080/top1000/prev", {
        method: 'POST',
        credentials: 'include'
    })
      .then((response) => response.text())
      .then((response) => {
        setWord(response);
      })
      .catch(() => {
        setWord("Something wrong with internet connection");
      });
  };

  return (
    <div className="center">
    <Card sx={{ minWidth: 255, maxWidth: 255, margin: "auto"}}>
      <CardContent>
        <Typography
          sx={{ marginLeft: "auto", fontSize: 12 }}
          color="text.secondary"
          gutterBottom
        >
          1 / 1000 word
        </Typography>
        <Typography sx={{ fontSize: 14 }} color="text.secondary" gutterBottom>
          Do you know:
        </Typography>
        <Typography variant="h5" component="div">
          {word}
        </Typography>
      </CardContent>
      <CardActions>
        <Stack direction="row" spacing={2}>
          <Button variant="contained">back</Button>
          <ButtonGroup
            disableElevation
            variant="contained"
            aria-label="Disabled elevation buttons"
          >
            <Button onClick={prev}>dont</Button>
            <Button onClick={next}>know</Button>
          </ButtonGroup>
        </Stack>
      </CardActions>
    </Card>
    </div>
  );
}

export default App;