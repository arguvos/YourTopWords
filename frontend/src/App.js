import './App.css';
import {useState} from "react";
import Card from "@mui/material/Card";
import CardActions from "@mui/material/CardActions";
import CardContent from "@mui/material/CardContent";
import Button from "@mui/material/Button";
import Typography from "@mui/material/Typography";
import ButtonGroup from "@mui/material/ButtonGroup";
import Stack from "@mui/material/Stack";


function App() {
    const [word, setWord] = useState("Click know to start");
    const [number, setNumber] = useState(0);
    const [renderDownload, setRenderDownload] = useState(false);

    const next = (value) => {
        let isKnow = new URLSearchParams();
        isKnow.append('isKnow', value);
        fetch("http://localhost:8080/top1000/next", {
            method: 'POST',
            credentials: 'include',
            body: isKnow
        })
            .then((response) => response.json())
            .then((response) => {
                setWord(response.word);
                setNumber(response.number);
                setRenderDownload(response.finish);
                if (response.finish) {
                    setWord("Finished, for show words click button");
                }
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
            .then((response) => response.json())
            .then((response) => {
                setWord(response.word);
                setNumber(response.number);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const reset = () => {
        fetch("http://localhost:8080/top1000/reset", {
            method: 'POST',
            credentials: 'include'
        })
            .then((response) => {
                setWord("Click know to start");
                setNumber(0);
                setRenderDownload(false);
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const unknowwords = () => {
        fetch("http://localhost:8080/top1000/unknowwords", {
            method: 'POST',
            credentials: 'include'
        })
            .then((response) => response.json())
            .then((response) => {
                setWord(response.join(", "));
            })
            .catch(() => {
                setWord("Something wrong with internet connection");
            });
    };

    const anki = () => {
        fetch("http://localhost:8080/top1000/anki?" + new URLSearchParams({
                lang: 'ru',
            }), {
            method: 'GET',
            credentials: 'include',
        });
    };

    return (
        <div className="center">
            <Card sx={{minWidth: 300, maxWidth: 300, margin: "auto"}}>
                <CardContent>
                    <Stack direction="row" spacing={2}>
                        <Typography
                            sx={{marginRight: "auto", fontSize: 12}}
                            color="text.secondary"
                            gutterBottom
                        >
                            {number} / 1000 word
                        </Typography>
                        <Button sx={{marginLeft: "auto", fontSize: 10}}
                                variant="outlined"
                                onClick={reset}>reset</Button>
                    </Stack>
                    <Typography sx={{fontSize: 14}} color="text.secondary" gutterBottom>
                        Do you know:
                    </Typography>
                    <Typography variant="h5" component="div">
                        {word}
                    </Typography>
                </CardContent>
                {
                    !renderDownload &&
                    <CardActions>
                        <Button sx={{marginRight: "auto"}} variant="contained" onClick={prev}>back</Button>
                        <ButtonGroup
                            disableElevation
                            variant="contained"
                            aria-label="Disabled elevation buttons"
                            sx={{marginLeft: "auto"}}
                        >
                            <Button onClick={() => next(false)}>dont</Button>
                            <Button onClick={() => next(true)}>know</Button>
                        </ButtonGroup>
                    </CardActions>
                }
                {
                    renderDownload &&
                    <CardActions>
                        <Button sx={{margin: "auto", width: "auto"}} variant="contained" color="success" onClick={unknowwords}>
                            SHOW UNKNOW WORDS</Button>
                        <Button sx={{margin: "auto", width: "auto"}} variant="contained" color="success" onClick={anki}>
                            DOWNLOAD ANKI</Button>
                    </CardActions>
                }
            </Card>
        </div>
    );
}

export default App;